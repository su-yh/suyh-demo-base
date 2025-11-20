package com.web.sys.excel.export;

import com.web.sys.excel.annotation.Excel;
import lombok.Data;

/**
 * @author suyh
 * @since 2025-06-19
 */
@Data
public class ExportSuperDto {
    @Excel(sort = 0, name = "id", colWidth = 5)
    private Long id;
}
