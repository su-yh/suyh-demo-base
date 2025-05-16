package com.suyh.base.mp.typehandler.enlist;

import com.suyh.base.mp.constants.enums.HourOfDayEnums;

/**
 * 这个只是一个示例
 *
 * @author suyh
 * @since 2024-09-03
 */
public class HourOfDayListTypeHandler extends AbstractEnumListTypeHandler<HourOfDayEnums> {
    public HourOfDayListTypeHandler() {
        super(HourOfDayEnums.class);
    }
}
