package com.lan.lojbackendmodelservice.vo;

import com.google.gson.Gson;
import lombok.Data;

import java.io.Serializable;

@Data
public class ExecInfo implements Serializable {
    //执行时间
    private Long time;
    //执行占用内存
    private Long memory;
    //执行信息
    private String message;

    /**
     * json转ExecInfo类
     * @param str
     * @return
     */
    public static ExecInfo strToObj(String str){
        Gson gson = new Gson();
        return gson.fromJson(str, ExecInfo.class);

    }
    /**
     * ExecInfo转json
     * @param execInfo
     * @return
     */
    public static String objToStr(ExecInfo execInfo){
        Gson gson = new Gson();
        return gson.toJson(execInfo);
    }


}
