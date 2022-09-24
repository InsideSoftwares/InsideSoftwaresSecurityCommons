package br.com.insidesoftwares.securitycommons.filter;


import br.com.insidesoftwares.securitycommons.utils.FilterUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
@RequiredArgsConstructor
@Log4j2
public class CorsFilter implements Filter {

    private final FilterUtils filterUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        filterUtils.enableCors(response);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {}
}
