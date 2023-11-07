package pl.ligatenisaziemnego.server.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

@Service
public class LoginCustomDsl extends AbstractHttpConfigurer<LoginCustomDsl, HttpSecurity> {
    private final RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;
    private final RestAuthenticationFailureHandler restAuthenticationFailureHandler;


    public LoginCustomDsl(RestAuthenticationSuccessHandler restAuthenticationSuccessHandler,
            RestAuthenticationFailureHandler restAuthenticationFailureHandler) {
        this.restAuthenticationSuccessHandler = restAuthenticationSuccessHandler;
        this.restAuthenticationFailureHandler = restAuthenticationFailureHandler;

    }

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        JsonObjectAuthenticationFilter filter = new JsonObjectAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl("/api/v1/auth/login");
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    public static LoginCustomDsl loginCustomDsl(RestAuthenticationSuccessHandler restAuthenticationSuccessHandler,
            RestAuthenticationFailureHandler restAuthenticationFailureHandler) {
        return new LoginCustomDsl(restAuthenticationSuccessHandler, restAuthenticationFailureHandler);
    }
}