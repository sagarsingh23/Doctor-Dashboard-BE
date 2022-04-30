package com.dashboard.doctor_dashboard.entity.report;

public class ResponseFile {
    public String name;
    public String url;
    public String type;
    public long size;

    public ResponseFile(String name, String url, String type, long size) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.size = size;
    }
}