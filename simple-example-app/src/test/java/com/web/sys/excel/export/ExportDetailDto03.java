package com.web.sys.excel.export;

import com.web.sys.excel.annotation.Excel;
import lombok.Data;

@Data
public class ExportDetailDto03 {
    @Excel(sort = 3, name = "exportField")
    private String exportField;
}
