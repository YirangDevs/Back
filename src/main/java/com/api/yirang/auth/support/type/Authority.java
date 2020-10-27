package com.api.yirang.auth.support.type;

public enum Authority {
    ROLE_VOLUNTEER("VOLUNTEER", "Has No Permission"),
    ROLE_ADMIN("ADMIN", "HAS All Permissions");

    private final String autohrity;
    private final String description;

    Authority(String autohrity, String description) {
        this.autohrity = autohrity;
        this.description = description;
    }

    @Override
    public String toString() {
        return autohrity;
    }

}
