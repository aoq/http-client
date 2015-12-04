/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.dev.http.content;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class holds parameters and converts them to the specified format if needed.
 */
public class ParametersBody extends AbstractMessageBody {

    private final Map<String, String> mParameters;

    public ParametersBody(Map<String, String> parameters) {
        mParameters = parameters;
    }

    @Override
    public String toJson() {
        JSONObject jsonObj = new JSONObject(mParameters);
        return jsonObj.toString();
    }

    @Override
    public String toParameter() {
        StringBuilder builder = new StringBuilder();
        Set<Entry<String, String>> entries = mParameters.entrySet();
        boolean first = true;
        for (Entry<String, String> entry : entries) {
            if (first) {
                first = false;
            } else {
                builder.append("&");
            }
            String key = entry.getKey();
            String value = entry.getValue();
            builder.append(key);
            builder.append("=");
            builder.append(value);
        }
        return builder.toString();
    }

    @Override
    public String toEncodedParameter() throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        Set<Entry<String, String>> entries = mParameters.entrySet();
        boolean first = true;
        for (Entry<String, String> entry : entries) {
            if (first) {
                first = false;
            } else {
                builder.append("&");
            }
            String key = entry.getKey();
            String value = entry.getValue();
            builder.append(key);
            builder.append("=");
            String encoded = URLEncoder.encode(value, "UTF-8");
            builder.append(encoded);
        }
        return builder.toString();
    }
}
