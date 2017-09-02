package com.business.parser.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WalmartQueryService {

    private static final String TEMPLATE = "http://api.walmartlabs.com/v1/search?apiKey=";
    private static final String API_KEY = "depnxpwy5j34kpa4eykqa6vs";

    public String getStringForProduct(String itemName) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(itemName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        return TEMPLATE + API_KEY + "&query=" + encoded + "&responseGroup=full&format=xml&numItems=25";
    }
}
