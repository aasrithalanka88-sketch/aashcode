package com.skillswap.util;

import com.skillswap.entity.User;
import jakarta.servlet.http.HttpSession;

public final class SessionHelper {

    public static final String SESSION_USER_ID_KEY = "LOGGED_IN_USER_ID";
    public static final String SESSION_USER_NAME_KEY = "LOGGED_IN_USER_NAME";
    public static final String SESSION_USER_EMAIL_KEY = "LOGGED_IN_USER_EMAIL";

    private SessionHelper() {
    }

    public static void setLoggedInUser(HttpSession session, User user) {
        if (session != null && user != null) {
            session.setAttribute(SESSION_USER_ID_KEY, user.getId());
            session.setAttribute(SESSION_USER_NAME_KEY, user.getName());
            session.setAttribute(SESSION_USER_EMAIL_KEY, user.getEmail());
        }
    }

    public static Long getLoggedInUserId(HttpSession session) {
        if (session == null) return null;
        Object id = session.getAttribute(SESSION_USER_ID_KEY);
        return id instanceof Long ? (Long) id : null;
    }

    public static boolean isLoggedIn(HttpSession session) {
        return getLoggedInUserId(session) != null;
    }

    public static void clear(HttpSession session) {
        if (session != null) {
            session.removeAttribute(SESSION_USER_ID_KEY);
            session.removeAttribute(SESSION_USER_NAME_KEY);
            session.removeAttribute(SESSION_USER_EMAIL_KEY);
            session.invalidate();
        }
    }
}
