package com.dashboard.doctor_dashboard.entity.report;

public class ResponseFile {
    private final String name;
    private final String url;
    private final String type;
    private final long size;

    public ResponseFile(String name, String url, String type, long size) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.size = size;
    }
}