package com.jwd.cafe.command;

import com.jwd.cafe.constant.RequestConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder(setterPrefix = "with")
@AllArgsConstructor
public class RequestContext {
    private Map<String, String> requestParameters;

    private Map<String, Object> requestAttributes;

    private Map<String, Object> sessionAttributes;

    private Map<String, String> cookies;

    private Map<String, Part> requestParts;

    private String locale;

    public RequestContext() {
    }

    public RequestContext(HttpServletRequest request) throws IOException, ServletException {
        requestParameters = extractRequestParameters(request);
        requestAttributes = extractRequestAttributes(request);
        sessionAttributes = extractSessionAttributes(request);
        cookies = extractCookies(request);
        requestParts = extractRequestParts(request);
        locale = extractLocale(request);
    }

    private Map<String, String> extractRequestParameters(HttpServletRequest request) {
        Map<String, String> parametersMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            parametersMap.put(parameterName, request.getParameter(parameterName));
        }

        return parametersMap;
    }

    private Map<String, Object> extractRequestAttributes(HttpServletRequest request) {
        Map<String, Object> attributes = new HashMap<>();
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            attributes.put(attributeName, request.getAttribute(attributeName));
        }
        return attributes;
    }

    private Map<String, Part> extractRequestParts(HttpServletRequest request) throws IOException, ServletException {
        Map<String, Part> parts = new HashMap<>();
        if (request.getContentType() != null && request.getContentType().toLowerCase().contains("multipart/form-data")) {
            for (Part part : request.getParts()) {
                parts.put(part.getName(), part);
            }
        }
        return parts;
    }

    private Map<String, String> extractCookies(HttpServletRequest request) {
        Map<String, String> cookiesMap = new HashMap<>();
        Cookie[] cookiesArray = request.getCookies();
        if (cookiesArray != null && cookiesArray.length != 0) {
            Arrays.stream(cookiesArray).forEach(cookie -> cookiesMap.put(cookie.getName(), cookie.getValue()));
        }
        return cookiesMap;
    }

    private Map<String, Object> extractSessionAttributes(HttpServletRequest request) {
        Map<String, Object> sessionAttributes = new HashMap<>();
        Enumeration<String> attributeNames = request.getSession(true).getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            sessionAttributes.put(attributeName, request.getSession().getAttribute(attributeName));
        }
        return sessionAttributes;
    }

    private String extractLocale(HttpServletRequest request) {
        String locale = (String) request.getSession().getAttribute(RequestConstant.LOCALE);
        return locale != null ? locale : "ru_RU";
    }
}