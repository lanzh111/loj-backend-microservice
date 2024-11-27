package com.lan.lojbackendjudgeservice.codeSandBox;


import com.lan.lojbackendjudgeservice.codeSandBox.strategy.CodeSandBoxStrategy;
import com.lan.lojbackendjudgeservice.codeSandBox.strategy.Imp.JavaCodeSandBoxStrategy;
import org.springframework.stereotype.Service;

@Service
public class CodeSandBoxManger {


    public CodeSandBoxStrategy getCodeSandBoxByLanguage(String type){
        if (type.equals("java")){
            return new JavaCodeSandBoxStrategy();
        }
        return null;
    }
}
