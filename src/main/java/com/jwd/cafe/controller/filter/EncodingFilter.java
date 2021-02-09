package com.jwd.cafe.controller.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

/**
 * The class changes {@link com.jwd.cafe.command.RequestContext} encoding
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@WebFilter(filterName = "Encoding", urlPatterns = {"/*"}, initParams = {@WebInitParam(name = "encoding",
        value = "UTF-8", description = "Encoding Param")})
public class EncodingFilter implements Filter {
    private String encoding;

    public void destroy() {
        encoding = null;
    }

    public void init(FilterConfig config) {
        encoding = config.getInitParameter("encoding");

        ServletContext context = config.getServletContext();

        FilterRegistration reqXssFilter = context.getFilterRegistration("XssAttackFilter");
        reqXssFilter.addMappingForUrlPatterns(null, true, "/*");

        FilterRegistration reqAuthFilter = context.getFilterRegistration("AuthFilter");
        reqAuthFilter.addMappingForUrlPatterns(null, true, "/rest", "/cafe");
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String codeRequest = request.getCharacterEncoding();
        if (codeRequest == null || !codeRequest.equalsIgnoreCase(encoding)) {
            request.setCharacterEncoding(encoding);
        }
        response.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }
}