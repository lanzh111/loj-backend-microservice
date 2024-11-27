package com.lan.lojbackendcommonservice.dto.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *

 */
public enum ResultStatusEnum {

    /**
     *
     * 执行通过（accepted)、错误解答(Wrong Answer)
     * 超出内存限制(memory limit exceeded )、超出时间限制(time limit exceeded)、
     * 超出输出限制(output limit exceeded )内部出错(external error)
     * 编译出错(compiler error)、运行出错(runtime Error)、超时 (overTime)
     *
     */
    ACCEPTED("accepted","执行通过" ),
    WRONG_ANSWER( "Wrong Answer","错误解答"),
    MEMORY_LIMIT_EXCEEDED("memory limit exceeded","超出内存限制"),
    TIME_LIMIT_EXCEEDED("time limit exceeded","超出时间限制"),
    OUTPUT_LIMIT_EXCEEDED("output limit exceeded","超出输出限制"),
    EXTERNAL_ERROR("external error","内部错误"),
    COMPILER_ERROR("compiler error","编译错误"),
    RUNTIME_ERROR("runtime error","执行错误"),
    OVERTIME("overTime","超时");


    private final String text;

    private final String value;

    ResultStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ResultStatusEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ResultStatusEnum anEnum : ResultStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
