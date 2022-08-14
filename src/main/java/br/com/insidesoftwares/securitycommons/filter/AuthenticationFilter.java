package br.com.insidesoftwares.securitycommons.filter;


import br.com.insidesoftwares.securitycommons.dto.AuthenticationDTO;
import br.com.insidesoftwares.securitycommons.implementation.AuthenticationSecurityService;
import br.com.insidesoftwares.securitycommons.utils.AuthenticationUtils;
import br.com.insidesoftwares.securitycommons.utils.FilterUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
@Log4j2
public class AuthenticationFilter extends GenericFilterBean {

    private final AuthenticationSecurityService authenticationSecurityService;
    private final FilterUtils filterUtils;

    private static final Pattern pattern = Pattern.compile("(api\\/v([0-9])+)");

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        filterUtils.setLocale(request);
        filterUtils.enableCors(response);

        if(isValidationJWT(request.getServletPath())) {
            AuthenticationDTO authenticationDTO = authenticationSecurityService.authentication(request);

            if (!authenticationDTO.isValid()) {
                filterUtils.setUnauthorizedResponse(response,authenticationDTO.getJwtErro());
                return;
            }

            SecurityContextHolder.getContext().setAuthentication(authenticationDTO.getAuthentication());
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidationJWT(String path){
        Matcher matcher = pattern.matcher(path);
        return matcher.find();
    }

}
