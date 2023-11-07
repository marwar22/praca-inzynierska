package pl.ligatenisaziemnego.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.ligatenisaziemnego.server.applicationuser.ApplicationUserRepository;

import java.io.IOException;
import java.util.Date;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        String username = authentication.getName();
        applicationUserRepository.updateLastLogin(username, new Date());
        response.setHeader("Content-Type", "application/json");
        var statusDTO = new AuthStatusDto(authentication);
        response.getWriter().write(objectMapper.writeValueAsString(statusDTO));
        clearAuthenticationAttributes(request);
    }
}