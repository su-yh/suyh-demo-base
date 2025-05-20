package com.base.mp.typehandler.enlist;

import com.base.mp.constants.enums.DaysOfWeekExampleEnums;

/**
 * 这个只是一个示例
 *
 * @author suyh
 * @since 2024-09-03
 */
public class DaysOfWeekListTypeHandler extends AbstractEnumListTypeHandler<DaysOfWeekExampleEnums> {
    public DaysOfWeekListTypeHandler() {
        super(DaysOfWeekExampleEnums.class);
    }
}
