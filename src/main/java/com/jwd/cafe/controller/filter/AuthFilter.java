package com.jwd.cafe.controller.filter;

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

/**
 * The class filters {@link com.jwd.cafe.command.RequestContext} and blocks users without access
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
@WebFilter(urlPatterns = {"/cafe", "/rest"},
        initParams = {@WebInitParam(name = "COMMAND", value = "to-access-blocked")})
public class AuthFilter implements Filter {

    private String blockedAccessPageCommand;

    @Override
    public void init(FilterConfig filterConfig) {
        blockedAccessPageCommand =
                filterConfig.getServletContext().getContextPath() + filterConfig.getInitParameter("COMMAND");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        request.setCharacterEncoding("UTF-8");
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(RequestConstant.USER);
        if (hasAccess(request, user)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("access blocked");
            response.sendRedirect(request.getServletPath() + "?command=" + blockedAccessPageCommand);
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
        blockedAccessPageCommand = null;
    }
}