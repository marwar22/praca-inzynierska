package pl.ligatenisaziemnego.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static pl.ligatenisaziemnego.server.security.LoginCustomDsl.loginCustomDsl;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

    private final RestAuthenticationFailureHandler restAuthenticationFailureHandler;

    private final RestAccessDeniedHandler restAccessDeniedHandler;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public SecurityConfig(RestAuthenticationSuccessHandler restAuthenticationSuccessHandler,
            RestAuthenticationFailureHandler restAuthenticationFailureHandler,
            RestAccessDeniedHandler restAccessDeniedHandler,
            RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationSuccessHandler = restAuthenticationSuccessHandler;
        this.restAuthenticationFailureHandler = restAuthenticationFailureHandler;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = getCorsConfiguration();

        http.csrf(AbstractHttpConfigurer::disable)
            .cors(corsConfigurer -> corsConfigurer.configurationSource(request -> corsConfiguration))
            .exceptionHandling(configurer -> configurer.accessDeniedHandler(restAccessDeniedHandler)
                                                       .authenticationEntryPoint(restAuthenticationEntryPoint))
            .securityContext((securityContext) -> securityContext.requireExplicitSave(false))
//            TODO update deprecated and(), when with() is in stable release of spring-boot-starter-security
//            https://github.com/spring-projects/spring-security/pull/13432
            .apply(loginCustomDsl(restAuthenticationSuccessHandler, restAuthenticationFailureHandler)).and()
            .logout(configurer -> configurer.logoutUrl("/api/v1/auth/logout")
                                            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)));
        return http.build();
    }

    private static CorsConfiguration getCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(
                List.of("http://localhost:3000", "https://localhost"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        return corsConfiguration;
    }
}