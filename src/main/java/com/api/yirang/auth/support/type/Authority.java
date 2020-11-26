package com.api.yirang.auth.support.type;

public enum Authority {
    ROLE_VOLUNTEER("VOLUNTEER", "Has No Permission"),
    ROLE_ADMIN("ADMIN", "HAS All Permissions"),
    ROLE_SUPER_ADMIN("SUPER_ADMIN", "HAS All Without Limitation");

    private final String authority;
    private final String description;

    Authority(String authority, String description) {
        this.authority = authority;
        this.description = description;
    }

    @Override
    public String toString() {
        return authority;
    }

}
