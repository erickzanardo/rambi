package com.rambi.core;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RambiFilter implements Filter {

    private static final String FILE_PATTERN = "FILE-PATTERN";

    private FilterConfig config;
    private String filePattern;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
            ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String requestURI = request.getRequestURI();
        // First of all, we should verify if this url matches out pattern
        if (filePattern != null && !requestURI.matches(filePattern)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        ServletContext servletContext = config.getServletContext();

        InputStream inputStream = servletContext.getResourceAsStream(requestURI);

        if (inputStream != null) {
            RambiScriptMachine.getInstance().executeHttpRequest(request, response, inputStream, requestURI,
                    servletContext);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.config = config;
        filePattern = config.getInitParameter(FILE_PATTERN);
    }

}
