package com.example.springsecurityjwt.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseObject {
    protected Boolean valid;
    protected String message;
    protected Object data;
}
