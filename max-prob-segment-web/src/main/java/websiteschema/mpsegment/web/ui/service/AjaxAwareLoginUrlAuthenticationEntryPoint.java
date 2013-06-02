package websiteschema.mpsegment.web.ui.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AjaxAwareLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("plain/text; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.append("Your request is not authenticated. If you would like to continue, you should sign up first.");
        } else {
            super.commence(request, response, authException);
        }
    }
}
