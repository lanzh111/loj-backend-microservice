package com.lan.lojbackendmodelservice.vo;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 *  判题用例
 *
 */
@Data
public class JudgeCase implements Serializable {
    //输入用例
    private String input;
    //输出用例
    private String output;


    /**
     * json转JudgeCase类
     * @param str
     * @return
     */
    public static JudgeCase strToObj(String str){
        Gson gson = new Gson();

        return gson.fromJson(str, JudgeCase.class);

    }

    /**
     * json转JudgeCase类
     * @param str
     * @return
     */
    public static List<JudgeCase> strToList(String str){
        Gson gson = new Gson();
        Type listType = new TypeToken<List<JudgeCase>>() {}.getType();
        return gson.fromJson(str, listType);
    }
    /**
     * json转JudgeCase类
     * @param caseList
     * @return
     */
    public static String listToStr(List<JudgeCase> caseList){
        Gson gson = new Gson();
        return gson.toJson(caseList);

    }


    /**
     * JudgeCase转json
     * @param judgeCase
     * @return
     */
    public static String objToStr(JudgeCase judgeCase){
        Gson gson = new Gson();
        return gson.toJson(judgeCase);
    }


}
