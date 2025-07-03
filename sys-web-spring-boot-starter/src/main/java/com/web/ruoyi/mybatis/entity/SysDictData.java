package com.web.ruoyi.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.ruoyi.constants.enums.DefaultEnums;
import com.web.ruoyi.constants.enums.StatusEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_dict_data", autoResultMap = true)
public class SysDictData {

    /**
     * 字典编码
     */
    @Schema(description = "主键ID")
    @TableId(value = "dict_code", type = IdType.AUTO)
    private Long dictCode;

    /**
     * 字典排序
     */
    @Schema(description = "排序值，初始时要留有空间，一般使用100，200，300这样的数字")
    private Integer dictSort;

    /**
     * 字典标签
     */
    @Schema(description = "前端用于展示的字符串")
    private String dictLabel;

    /**
     * 字典键值
     */
    private String dictValue;

    /**
     * 字典类型
     */
    @Schema(description = "与字典类型表关联，前端会使用的全局唯一常量值")
    private String dictType;

    /**
     * 样式属性（其他样式扩展）
     */
    @Schema(description = "这个目前暂时没用，后面如果需要再处理")
    @JsonIgnore
    private String cssClass;

    /**
     * 表格字典样式
     */
    private String listClass;

    @JsonIgnore
    @TableField("is_default")
    private DefaultEnums def;

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
    @JsonIgnore // 暂时不给前端使用
    @SuppressWarnings("unused")
    public boolean getIsDefault() {
        return def == DefaultEnums.YES;
    }

    /**
     * 用于json 处理解决序列化给前端使用，不允许随便修改该方法名
     */
    @JsonIgnore // 给前端的都是可用的
    public boolean isEnabled() {
        return st == StatusEnums.ENABLE;
    }

}
