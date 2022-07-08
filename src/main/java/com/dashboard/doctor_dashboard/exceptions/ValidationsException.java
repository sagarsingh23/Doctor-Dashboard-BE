package com.dashboard.doctor_dashboard.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
@AllArgsConstructor
@Getter
public class ValidationsException extends RuntimeException {
    private final List<String>  messages;
}
