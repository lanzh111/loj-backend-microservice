package com.lan.lojbackendcommonservice.utils;

import java.util.Arrays;

public class StringUtil {

    public static int[] parseIntArray(String str){
        if (str==null || str.length()<=2){
            return new int[]{};
        }
        str =str.substring(1,str.length()-1);
       return Arrays.stream(str.split(",")).mapToInt(Integer::parseInt).toArray();
    }
}
