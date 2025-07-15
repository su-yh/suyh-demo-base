package com.web.sys.excel.export;

import com.web.sys.excel.annotation.Excel;
import lombok.Data;

/**
 * @author suyh
 * @since 2025-06-19
 */
@Data
public class ExportParentDto extends ExportSuperDto {
    @Excel(sort = 2, name = "uuid")
    private String uuid;
}
