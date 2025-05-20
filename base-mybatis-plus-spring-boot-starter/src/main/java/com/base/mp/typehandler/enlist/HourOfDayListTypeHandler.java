package com.base.mp.typehandler.enlist;

import com.base.mp.constants.enums.HourOfDayExampleEnums;

/**
 * 这个只是一个示例
 *
 * @author suyh
 * @since 2024-09-03
 */
public class HourOfDayListTypeHandler extends AbstractEnumListTypeHandler<HourOfDayExampleEnums> {
    public HourOfDayListTypeHandler() {
        super(HourOfDayExampleEnums.class);
    }
}
