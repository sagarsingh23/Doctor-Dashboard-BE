package com.dashboard.doctor_dashboard.Utils.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericMessage {
    private String status;
    private Object data;
}
