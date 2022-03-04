package com.kwambi.employeesystem.constant;


public class SecurityConstant {
    public static final long EXPIRATION_TIME = 600000; //10min EXPRESSED IN MILLISECONDS
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "jwt-Token"; //CUSTOM HEADER
    public static final String TOEKN_CANNOT_BE_VERIFIED = "Token Cannot Be Verified";
    public static final String KWAMBI_LLC = "Kwambi, LLC"; //issuer
    public static final String KWAMBI_ADMINISTRATION = "User Management Portal"; //Audiance or who will be using the token
    public static final String AUTHORITIES = "authorities"; //all the authorities of the user
    public static final String FORBIDDEN_MESSAGE= "You need to login to access this page";
    public static final String ACCESS_DENIED_MESSAGE ="You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = { "user/login/", "/user/register", "user/resetpassword/**", "user/image/**" };

    //public static final String[] PUBLIC_URLS = { "**" };

    
}
