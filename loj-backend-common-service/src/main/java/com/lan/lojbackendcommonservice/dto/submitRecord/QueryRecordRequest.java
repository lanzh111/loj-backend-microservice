package com.lan.lojbackendcommonservice.dto.submitRecord;

import com.lan.lojbackendcommonservice.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询提交记录
 */
@Data
public class QueryRecordRequest extends PageRequest implements Serializable {


    /**
     * 编程语言 枚举值
     */
    private Integer language;



    /**
     * 结果状态
     */
    private String resultStates;

    /**
     * 结果信息
     */
    private String resultInfo;

    /**
     * 题目id
     */
    private Long questionId;


}
