package pl.ligatenisaziemnego.server.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;

public class JsonObjectAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        LoginDto authRequest = new LoginDto("", "");
        try {
            BufferedReader reader = request.getReader();
            var stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            authRequest = objectMapper.readValue(stringBuilder.toString(), LoginDto.class);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                authRequest.getUsername(), authRequest.getPassword()
        );
        setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);
    }
}