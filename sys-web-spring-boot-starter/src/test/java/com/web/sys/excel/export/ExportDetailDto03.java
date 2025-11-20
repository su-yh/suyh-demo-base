package com.web.sys.excel.export;

import com.ebusiness.rouyi.excel.annotation.Excel;
import lombok.Data;

@Data
public class ExportDetailDto03 {
    @Excel(sort = 3, name = "exportField", colWidth = 15)
    private String exportField;
}
