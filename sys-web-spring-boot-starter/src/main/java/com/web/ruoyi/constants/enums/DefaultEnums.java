package com.web.ruoyi.constants.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author suyh
 * @since 2025-07-01
 */
@Getter
public enum DefaultEnums {
    YES("Y"),
    NO("N"),
    ;

    @EnumValue
    private final String code;

    DefaultEnums(String code) {
        this.code = code;
    }
}
