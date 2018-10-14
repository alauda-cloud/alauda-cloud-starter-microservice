package io.alauda.stater.microservice.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Order(1)
@WebFilter(urlPatterns = "/*",filterName = "JwtSecurityFilter")
public class JwtSecurityFilter implements Filter {

    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        JwtAuthentication jwtAuthentication = new JwtAuthentication(SecurityContextHolder.getContext().getAuthentication(),jwtAccessTokenConverter);
        SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
