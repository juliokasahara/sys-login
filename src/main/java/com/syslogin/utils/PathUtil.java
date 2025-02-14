package com.syslogin.utils;

import javax.servlet.http.HttpServletRequest;

public class PathUtil {
    public static String getFrontURL(HttpServletRequest request) {
        var serverName = request.getScheme() + "://" + request.getServerName();
        return serverName;
    }
}
