package kz.ssss.filo.util;

public class Constant {

    public static final String BASE_AUTH_URL = "/auth";
    public static final String HOME_URL = "/";
    public static final String SEARCH_URL = "/search";
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String REGISTER_ENDPOINT = "/register";
    public static final String FULL_LOGIN_URL = BASE_AUTH_URL + LOGIN_ENDPOINT;
    public static final String FULL_LOGOUT_URL = BASE_AUTH_URL + "/logout";

    public static final String AUTH_VIEW_PACKAGE = "/auth";
    public static final String LOGIN_PAGE = AUTH_VIEW_PACKAGE + "/login";
    public static final String REGISTER_PAGE = AUTH_VIEW_PACKAGE + "/register";

    public static final String SESSION_COOKIE_ATTRIBUTE = "JSESSIONID";
    public static final String USER_ATTRIBUTE = "user";

}
