package org.uwgb.compsci330.server;


public class Configuration {
    private Configuration() {}

    public static final String SERVER_VERSION = "0.0.1";
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_USERNAME_LENGTH = 2;
    public static final int MAX_USERNAME_LENGTH = 8;

    // Should be base 64 encoded!!
    // Default value is base64 encoded "this-passsword-is-super-duper-secret-and-secure-totally-no-one-will-ever-figure-it-out" should really be set in env to something secure, and not visible publicly.
    public static final String JWT_SECRET = System.getenv("JWT_SECRET") == null ? "dGhpcy1wYXNzc3dvcmQtaXMtc3VwZXItZHVwZXItc2VjcmV0LWFuZC1zZWN1cmUtdG90YWxseS1uby1vbmUtd2lsbC1ldmVyLWZpZ3VyZS1pdC1vdXQ=" : System.getenv("JWT_SECRET");
    public static final long JWT_EXPIRATION_MS = 1000 * 60 * 60 * 30; // 30 days till token expires.
}
