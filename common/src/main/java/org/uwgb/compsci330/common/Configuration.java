package org.uwgb.compsci330.common;

public class Configuration {
    protected Configuration() {}

    public static final String SYSTEM_USER = "System";
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_USERNAME_LENGTH = 2;
    public static final int MAX_USERNAME_LENGTH = 8;
    public static final int MAX_RESUME_BUFFER = 250;

    public static final int MAX_MESSAGE_FETCH_LIMIT = 500;
    public static final int MAX_MESSAGE_LENGTH = 4000;
}
