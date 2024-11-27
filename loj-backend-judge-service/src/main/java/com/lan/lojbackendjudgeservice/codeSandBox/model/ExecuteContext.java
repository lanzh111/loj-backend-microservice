package com.lan.lojbackendjudgeservice.codeSandBox.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class ExecuteContext implements Serializable {
    private String testCode;
    private String code;
}
