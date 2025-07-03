package com.web.ruoyi.constants.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author suyh
 * @since 2025-07-01
 */
@Getter
public enum StatusEnums {
    ENABLE("0"),
    DISABLE("1"),
    ;

    @EnumValue
    private final String code;

    StatusEnums(String code) {
        this.code = code;
    }
}
