package com.saphire.iopush.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {


    private static final ObjectMapper mapper = new ObjectMapper();


    private SecurityUtils() {
    }


    /**
     * Get the login of the current user.
     */
    public static String getCurrentLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails springSecurityUser = null;
        String userName = null;

        if(authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }

        return userName;
    }


    public static void sendError(HttpServletResponse httpresponse, Exception exception, int status, String message) throws IOException {
        httpresponse.setContentType("application/json;charset=UTF-8");
        httpresponse.setStatus(status);
        PrintWriter writer = httpresponse.getWriter();
        Response response = new Response() ;
        response.setErrorMessage(exception.getMessage());
        response.setStatus(false);
        response.setMessage(message);
        writer.write(mapper.writeValueAsString(response));
        writer.flush();
        writer.close();
    }


    public static void sendResponse(HttpServletResponse response, int status, Object object) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(mapper.writeValueAsString(object));
        response.setStatus(status);
        writer.flush();
        writer.close();
    }

}
