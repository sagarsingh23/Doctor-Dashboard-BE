package com.dashboard.doctor_dashboard.entity.report;

public class ResponseFile {
    public final String name;
    public final String url;
    public final String type;
    public final long size;

    public ResponseFile(String name, String url, String type, long size) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.size = size;
    }
}