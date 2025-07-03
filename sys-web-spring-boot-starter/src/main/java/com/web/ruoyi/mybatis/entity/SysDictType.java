package com.web.ruoyi.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.ruoyi.constants.enums.StatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_dict_type", autoResultMap = true)
public class SysDictType {
    /**
     * 字典主键
     */
    @Schema(description = "主键ID")
    @TableId(value = "dict_id", type = IdType.AUTO)
    private Long dictId;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    @Schema(description = "与字典数据表关联，前端会使用的全局唯一常量值")
    private String dictType;

    @JsonIgnore
    @TableField("status")
    private StatusEnums st;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者
     */
    @JsonIgnore
    private String createBy;

    /**
     * 创建时间
     */
    @JsonIgnore
    private Date createTime;

    /**
     * 更新者
     */
    @JsonIgnore
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonIgnore
    private Date updateTime;

    /**
     * 用于json 处理解决序列化给前端使用，不允许随便修改该方法名
     */
    @JsonIgnore // 给前端的都是可用的
    public boolean isEnabled() {
        return st == StatusEnums.ENABLE;
    }
}
