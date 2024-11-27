package com.lan.lojbackendjudgeservice.codeSandBox.strategy;


import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteContext;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteResponse;

public interface CodeSandBoxStrategy {
    ExecuteResponse Execute(ExecuteContext executeContext);
}
