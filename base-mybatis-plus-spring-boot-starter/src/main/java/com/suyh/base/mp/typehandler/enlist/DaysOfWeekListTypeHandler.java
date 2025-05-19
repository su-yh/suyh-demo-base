package com.suyh.base.mp.typehandler.enlist;

import com.suyh.base.mp.constants.enums.DaysOfWeekEnums;

/**
 * 这个只是一个示例
 *
 * @author suyh
 * @since 2024-09-03
 */
public class DaysOfWeekListTypeHandler extends AbstractEnumListTypeHandler<DaysOfWeekEnums> {
    public DaysOfWeekListTypeHandler() {
        super(DaysOfWeekEnums.class);
    }
}
