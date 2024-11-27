package com.lan.lojbackendjudgeservice.codeSandBox.Imp;

import com.lan.lojbackendcommonservice.common.ErrorCode;
import com.lan.lojbackendcommonservice.exception.BusinessException;
import com.lan.lojbackendjudgeservice.codeSandBox.CodeSandBox;
import com.lan.lojbackendjudgeservice.codeSandBox.CodeSandBoxManger;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteContext;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteRequest;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteResponse;
import com.lan.lojbackendjudgeservice.codeSandBox.strategy.CodeSandBoxStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * Java原生代码沙箱
 */
@Slf4j
public class JavaNativeCodeSandBox implements CodeSandBox {

   private final String fileName = "Main.java";
   private final String path ="src/main/resources/code/";

    private final CodeSandBoxManger codeSandBoxManger = new CodeSandBoxManger();
    @Override
    public ExecuteResponse ExecuteCode(ExecuteRequest executeRequest) {

        if (executeRequest==null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        String code = executeRequest.getCode();
        String testCode = executeRequest.getTestCode();
        String language = executeRequest.getLanguage();

        // 调用不同编程语言的代码沙箱执行实现
        CodeSandBoxStrategy codeSandBoxByLanguage = codeSandBoxManger.getCodeSandBoxByLanguage(language);
        ExecuteContext executeContext = new ExecuteContext();
        executeContext.setCode(code);
        executeContext.setTestCode(testCode);
        return codeSandBoxByLanguage.Execute(executeContext);

    }
}
