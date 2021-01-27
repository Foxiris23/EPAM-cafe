package com.jwd.cafe.controller.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    private static final String AVOID_SCRIPT_FRAGMENTS_PATTERN = "<script>(.*?)</script>";
    private static final String AVOID_SCRIPT_TAG_PATTERN = "</script>";
    private static final String AVOID_END_SCRIPT_PATTERN = "<script(.*?)>";
    private static final String AVOID_SRC_PATTERN = "src[\r\n]*=[\r\n]*\'(.*?)\'";
    private static final String AVOID_SRC_QUOTES_PATTERN = "src[\r\n]*=[\r\n]*\"(.*?)\"";
    private static final String AVOID_EVAL_PATTERN = "eval\\((.*?)\\)";
    private static final String AVOID_EXPRESSION_PATTERN = "expression\\((.*?)\\)";
    private static final String AVOID_JAVASCRIPT_PATTERN = "javascript:";
    private static final String AVOID_VBSCRIPT_PATTERN = "vbscript:";
    private static final String AVOID_ONLOAD_PATTERN = "onload(.*?)=";

    private static final Pattern[] patterns = new Pattern[]{
            Pattern.compile(AVOID_SCRIPT_FRAGMENTS_PATTERN, Pattern.CASE_INSENSITIVE),
            Pattern.compile(AVOID_SRC_PATTERN, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile(AVOID_SRC_QUOTES_PATTERN, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile(AVOID_SCRIPT_TAG_PATTERN, Pattern.CASE_INSENSITIVE),
            Pattern.compile(AVOID_END_SCRIPT_PATTERN, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile(AVOID_EVAL_PATTERN, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile(AVOID_EXPRESSION_PATTERN, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile(AVOID_JAVASCRIPT_PATTERN, Pattern.CASE_INSENSITIVE),
            Pattern.compile(AVOID_VBSCRIPT_PATTERN, Pattern.CASE_INSENSITIVE),
            Pattern.compile(AVOID_ONLOAD_PATTERN, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

    XssRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameters = new HashMap<>();
        super.getParameterMap().forEach((key, value) -> parameters.put(stripXSS(key), getParameterValues(key)));
        return parameters;
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);

        if (values == null) {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSS(values[i]);
        }

        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return stripXSS(value);
    }

    @Override
    public String getHeader(String name) {

        String value = super.getHeader(name);
        return stripXSS(value);
    }

    String stripXSS(String value) {
        if (value != null) {

            value = value.replaceAll("\0", "");

            for (Pattern scriptPattern : patterns) {
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }
}
