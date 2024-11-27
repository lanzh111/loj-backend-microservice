package com.lan.lojbackendjudgeservice.codeSandBox.Imp;


import cn.hutool.json.JSONUtil;
import com.lan.lojbackendjudgeservice.codeSandBox.CodeSandBox;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteRequest;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *
 * 远程代码沙箱
 */
public class RemoteCodeSandBox implements CodeSandBox {
    private   final String key = "ba6e4b5a-24cd-4428-ab54-d72ca12039a8";

    @Override
    public ExecuteResponse ExecuteCode(ExecuteRequest executeRequest) {
        String execRequestStr = JSONUtil.parse(executeRequest).toString();
        ExecuteResponse executeResponse;
        try {
            Response response = Request.Post("http://localhost:8850/codeSandbox/get").addHeader("authentication",key).bodyString(execRequestStr, ContentType.APPLICATION_JSON).execute();

            String re = response.returnContent().asString(StandardCharsets.UTF_8);
            executeResponse = JSONUtil.toBean(re, ExecuteResponse.class);
            return executeResponse;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
