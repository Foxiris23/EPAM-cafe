package com.jwd.cafe.controller.filter;

import com.jwd.cafe.constant.PageConstant;
import com.jwd.cafe.constant.RequestConstant;
import com.jwd.cafe.domain.Role;
import com.jwd.cafe.domain.User;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Log4j2
@WebFilter(urlPatterns = {"/cafe", "/rest"},
        initParams = {@WebInitParam(name = "PAGE_PATH", value = PageConstant.ACCESS_BLOCKED_PAGE)})
public class AuthFilter implements Filter {

    private String blockedAccessPagePath;

    @Override
    public void init(FilterConfig filterConfig) {
        blockedAccessPagePath =
                filterConfig.getServletContext().getContextPath() + filterConfig.getInitParameter("PAGE_PATH");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Auth filter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(RequestConstant.USER);
        if (hasAccess(request, user)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("access blocked");
            response.sendRedirect(request.getServletPath() + "?command=to-main");
        }
    }

    private boolean hasAccess(HttpServletRequest request, User user) {
        boolean result = true;
        String command = request.getParameter(RequestConstant.COMMAND);
        if (command != null) {
            if (user == null && (command.contains(RequestConstant.USER) || command.contains(RequestConstant.ADMIN))) {
                result = false;
            }
            if (user != null && !user.getRole().equals(Role.ADMIN) && command.contains(RequestConstant.ADMIN)) {
                result = false;
            }
        }
        return result;
    }

    @Override
    public void destroy() {
        blockedAccessPagePath = null;
    }
}