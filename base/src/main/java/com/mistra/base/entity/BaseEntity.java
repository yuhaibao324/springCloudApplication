package com.mistra.base.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: WangRui
 * @Date: 2018-09-14
 * Time: 上午11:05
 * Description:
 */
@Data
public class BaseEntity implements Serializable {

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;

    private Integer status;

    @Override
    public String toString() {
        return "BaseEntity{" +
                "createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createBy='" + createBy + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", status=" + status +
                '}';
    }
}
