package com.web.sys.excel.args;

import lombok.Data;

/**
 * @author suyh
 * @since 2025-05-14
 */
@Data
public class HandlerArgsBigDecimal implements HandlerArgs {
    public static final String JSON_DECIMAL_FORMAT_FIXED_TWO = "{\"decimalFormatPattern\": \"######0.00\"}";
    public static final String JSON_DECIMAL_FORMAT_PERCENTAGE = "{\"decimalFormatPattern\": \"0.00%\"}";

    // 格式化模式
    private String decimalFormatPattern;
}
