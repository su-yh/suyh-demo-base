package com.web.sys.excel.args;

import lombok.Data;

/**
 * @author suyh
 * @since 2025-05-14
 */
@Data
public class HandlerArgsDate implements HandlerArgs {
    // 格式化字符串
    private String dateFormat;
}
