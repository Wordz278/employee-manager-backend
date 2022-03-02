package com.kwambi.employeesystem.constant;

public class EmailConstant {

    // @Value("${email.credentials.username}")
    // private static String USERNAME;

    // @Value("${email.credentials.password}")
    // private static String PASSWORD;


    public static final String SIMPLE_MAIL_TRANSFER_PROTOCOL = "smtps";
    public static final String USERNAME = "your actual email";
    public static final String PASSWORD = "your actual email password";
    public static final String FROM_EMAIL = "support@kwambi.com";
    public static final String CC_EMAIL = "";
    public static final String EMAIL_SUBJECT = "Kwambi, LLC - New Password";    
    public static final String GMAIL_SMTP_SERVER = "smtp.gmail.com";
    public static final String SMTP_HOST = "mail.smtp.host";
    public static final String SMTP_AUTH = "mail.smtp.auth";
    public static final String SMTP_PORT = "mail.smtp.port";
    public static final int DEFAULT_PORT  = 465;
    public static final String SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
  

}
