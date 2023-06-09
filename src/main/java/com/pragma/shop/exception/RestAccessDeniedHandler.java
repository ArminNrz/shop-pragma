package com.pragma.shop.exception;

import com.pragma.shop.service.lowlevel.SecurityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final SecurityService securityService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException{
        securityService.handleAuthException(response, e, HttpStatus.FORBIDDEN);
    }
}
