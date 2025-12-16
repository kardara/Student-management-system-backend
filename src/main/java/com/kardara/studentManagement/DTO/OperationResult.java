package com.kardara.studentManagement.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OperationResult {

    private String message;
    private boolean success;

    public OperationResult(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public OperationResult(){}


}
