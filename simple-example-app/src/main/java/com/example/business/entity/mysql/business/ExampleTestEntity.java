package com.example.business.entity.mysql.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author suyh
 * @since 2024-09-02
 */
@Data
@TableName(value = "example_test", autoResultMap = true)
public class ExampleTestEntity {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键id")
    private Long id;

    @TableField("code")
    private String code;

    /**
     * 创建时间
     */
    @TableField("created")
    @Schema(description = "创建时间")
    private Date created;

    /**
     * 更新时间
     */
    @TableField("updated")
    @Schema(description = "更新时间")
    private Date updated;

}
