package com.pragma.shop.service.lowlevel;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.shop.constant.Constant;
import com.pragma.shop.entity.AppUser;
import com.pragma.shop.security.data.AuthorizationData;
import com.pragma.shop.security.handler.JwtHandler;
import com.pragma.shop.security.handler.TokenHandler;
import com.pragma.shop.service.entity.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Slf4j
@RequiredArgsConstructor
public class SecurityService {

    private final JwtHandler jwtHandler;
    private final TokenHandler tokenHandler;
    private final UserService userService;

    public String createAccessToken(HttpServletRequest request, String phoneNumber, List<String> roles) {
        return tokenHandler.createAccessToken(request, phoneNumber, roles);
    }

    public String createRefreshToken(HttpServletRequest request, User user) {
        return tokenHandler.createRefreshToken(request,user);
    }

    public void makeResponse(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        tokenHandler.makeResponse(response, accessToken, refreshToken);
    }

    public AuthorizationData extractAuthorizationData(String token) {

        DecodedJWT decodedJWT = jwtHandler.getDecodedJWT(token);
        String phoneNumber = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

        return new AuthorizationData(phoneNumber, roles);
    }

    public void refreshToken(String authorizationHeader, HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            makeRefreshToken(authorizationHeader, request, response);
        }
        else {
            throw Problem.valueOf(Status.BAD_REQUEST, "Refresh token is missing");
        }

    }

    private void makeRefreshToken(String authorizationHeader, HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            tokenHandler.buildRefreshToken(authorizationHeader, request, response);
        }
        catch (Exception exception) {
            handleUnAuthorizeException(exception, response);
        }
    }

    public void handleUnAuthorizeException(Exception exception, HttpServletResponse response) throws IOException {

        log.error("There is an error: {}", exception.getMessage());
        try {
            throw exception;
        }
        catch (AccessDeniedException accessDeniedException) {
            throw Problem.valueOf(Status.FORBIDDEN, accessDeniedException.getMessage());
        }
        catch (SignatureVerificationException | TokenExpiredException signatureVerificationException) {
            handleAuthException(response, signatureVerificationException, HttpStatus.FORBIDDEN);
            throw Problem.valueOf(Status.NOT_ACCEPTABLE, signatureVerificationException.getMessage());

        } catch (Exception e) {
            throw Problem.valueOf(Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void handleAuthException(HttpServletResponse response, RuntimeException failed, HttpStatus status) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(status.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        Map<String, Object> data = buildResponseError(failed.getMessage(), status);

        response.getOutputStream()
                .println(objectMapper.writeValueAsString(data));
    }

    private Map<String, Object> buildResponseError(String message, HttpStatus status) {
        Map<String, Object> data = new HashMap<>();
        data.put(
                "title",
                status.getReasonPhrase());
        data.put(
                "status",
                status.value());
        data.put(
                "detail",
                message);
        return data;
    }

    public AppUser getUserWithToken(String token) {

        token = token.substring("Bearer ".length());

        DecodedJWT decodedJWT = jwtHandler.getDecodedJWT(token);

        String phoneNumber = decodedJWT.getSubject();

        AppUser foundUser = userService.getByPhoneNumber(phoneNumber);
        if (foundUser == null) {
            throw Problem.valueOf(Status.NOT_FOUND, Constant.APP_USER_NOT_FOUND);
        }

        return foundUser;
    }

    public List<String> getTokenRoles(String token) {

        token = token.substring("Bearer ".length());

        DecodedJWT decodedJWT = jwtHandler.getDecodedJWT(token);

        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

        return List.of(roles);
    }
}
