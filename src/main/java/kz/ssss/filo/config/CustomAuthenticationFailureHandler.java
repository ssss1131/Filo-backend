package kz.ssss.filo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static kz.ssss.filo.util.Constant.BASE_AUTH_URL;
import static kz.ssss.filo.util.Constant.LOGIN_ENDPOINT;


@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String errorType = exception instanceof BadCredentialsException
                ? "Invalid username or password!"
                : "An internal error occurred. Please try again later.";
        
        request.getSession().setAttribute("flash_error_message", errorType);
        response.sendRedirect(request.getContextPath() + BASE_AUTH_URL + LOGIN_ENDPOINT);
    }

}
