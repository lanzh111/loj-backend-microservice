package com.lan.lojbackendmodelservice.vo;

import com.google.gson.Gson;
import lombok.Data;

/**
 * 判题配置（比如题目限制）
 */
@Data
public class JudgeConfig {

    //时间限制
    private Integer timeLimit;
    //内存限制
    private Integer memoryLimit;
    //堆栈限制
    private Integer stackLimit;



    /**
     * json转JudgeConfig类
     * @param str
     * @return
     */
    public static JudgeConfig strToObj(String str){
        Gson gson = new Gson();
        return gson.fromJson(str, JudgeConfig.class);

    }


    /**
     * JudgeConfig转json
     * @param judgeConfig
     * @return
     */
    public static String objToStr(JudgeConfig judgeConfig){
        Gson gson = new Gson();

        return  gson.toJson(judgeConfig);
    }

}
