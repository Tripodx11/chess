package service;

import java.util.UUID;

public class AuthTokenGen {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
