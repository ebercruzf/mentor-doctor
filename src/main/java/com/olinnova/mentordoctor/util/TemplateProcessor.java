package com.olinnova.mentordoctor.util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TemplateProcessor {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\s*([^}]+)\\s*\\}");

    public String processTemplate(String template, TemplateData data) {
        StringBuilder result = new StringBuilder();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        int lastIndex = 0;

        while (matcher.find()) {
            result.append(template, lastIndex, matcher.start());
            String placeholder = matcher.group(1).trim();
            String replacement = data.getValue(placeholder);
            result.append(replacement != null ? replacement : matcher.group());
            lastIndex = matcher.end();
        }

        result.append(template.substring(lastIndex));
        return result.toString();
    }
}


