package me.yingrui.segment.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("plain/text; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.append("Your Request is not Forbidden, Please Contact to Project Administrator.");
    }
}
