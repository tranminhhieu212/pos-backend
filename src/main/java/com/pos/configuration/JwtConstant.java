package com.pos.configuration;

public class JwtConstant {

    // Secret key for JWT signing - MUST be at least 256 bits (32 characters)
    // IMPORTANT: Change this to a secure random string in production!
    public static final String JWT_SECRET = "falkdfhlaskdffkjaldhsfjahslkdfhasdlfafhalsdfhalsdfhasldfheiowruqpwesdnfasvmansdv";

    // Header name for JWT token in HTTP requests
    public static final String JWT_HEADER = "Authorization";
}