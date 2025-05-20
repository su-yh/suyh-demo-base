package com.base.mp.constants.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 提供的一个示例
 *
 * @author suyh
 * @since 2024-09-03
 */
@Getter
public enum DaysOfWeekExampleEnums {

    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    @EnumValue
    private final int dayOfWeek;

    DaysOfWeekExampleEnums(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
