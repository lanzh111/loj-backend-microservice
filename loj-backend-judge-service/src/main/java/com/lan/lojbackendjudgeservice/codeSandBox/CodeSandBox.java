package com.lan.lojbackendjudgeservice.codeSandBox;

import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteRequest;
import com.lan.lojbackendjudgeservice.codeSandBox.model.ExecuteResponse;
/**
 * 代码沙箱接口
 */
public interface CodeSandBox {
    /**
     * 编译、执行代码
     * @param executeRequest
     * @return
     */
    ExecuteResponse ExecuteCode(ExecuteRequest executeRequest);
}
