package com.lan.lojbackendjudgeservice.codeSandBox.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行请求参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteRequest {
    private String code;
    private String testCode;
    private String language;
    private List<String> inputList;
}
