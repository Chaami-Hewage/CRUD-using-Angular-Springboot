package com.example.socialnetworkingapp.security;

public enum AccountPermission {
    USER_READ("user:read"), //permission to read user information and has the string value "user:read
    USER_WRITE("user:write"),
    POST_READ("post:read"),
    POST_WRITE("post:write");


    private final String permission;

    AccountPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
