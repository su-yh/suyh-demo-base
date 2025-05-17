package com.suyh.sys.web.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户对象 sys_user
 * 
 * @author ruoyi
 */
@Data
@TableName(value = "sys_user", autoResultMap = true)
public class SysUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public boolean isAdmin() {
        return id != null && 1L == id;
    }

    @TableId(type = IdType.AUTO)
    private Long id;

    @Size(max = 64)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 64)
    private String password;

    @Size(max = 64)
    private String nickname;

    @JsonIgnore
    private String salt;

    /**
     * 二次认证key
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String twoFactorAuthKey;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updated;
}
