package com.api.yirang.seniors.support.custom;

public enum ServiceType {

    SERVICE_WORK("WORK", "The Type Of Work That Need Lots Of Energy"),
    SERVICE_TALK("TALK", "The Type Of Work That Need little Energy"),;

    private final String type;
    private final String description;

    ServiceType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    @Override
    public String toString(){return type;}
}
