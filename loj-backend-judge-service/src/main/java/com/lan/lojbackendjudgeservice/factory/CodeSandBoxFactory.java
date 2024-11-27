package com.lan.lojbackendjudgeservice.factory;


import com.lan.lojbackendjudgeservice.codeSandBox.CodeSandBox;
import com.lan.lojbackendjudgeservice.codeSandBox.Imp.ExampleCodeSandBox;
import com.lan.lojbackendjudgeservice.codeSandBox.Imp.JavaNativeCodeSandBox;
import com.lan.lojbackendjudgeservice.codeSandBox.Imp.RemoteCodeSandBox;

/**
 * 静态代码沙箱工厂
 */
public class CodeSandBoxFactory {
    private static CodeSandBox codeSandBox;

    public CodeSandBoxFactory(CodeSandBox codeSandBox) {
        CodeSandBoxFactory.codeSandBox = codeSandBox;
    }

    public static CodeSandBox getInstance(String type){
       if (type.equals("example")){
           codeSandBox =new ExampleCodeSandBox();
       }else if (type.equals("remote")){
           codeSandBox =new RemoteCodeSandBox();
       }else if (type.equals("native")){
           codeSandBox =new JavaNativeCodeSandBox();
       }
       return codeSandBox;
   }
}
