package com.dashboard.doctor_dashboard.exception;

public class MyCustomException extends RuntimeException {
    private final String resourceName;
    private final String fieldName;
    private final long fieldValue;

    public MyCustomException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found in Doctor List with %s : %s", resourceName, fieldName, fieldValue)); // Post not found with id : 1
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
