package com.web.sys.excel.export;

import com.web.sys.excel.annotation.Excel;
import lombok.Data;

@Data
public class ExportDetailDto02 {
    @Excel(sort = 3, name = "dto03", minParseUnit = false)
    private ExportDetailDto03 dto03;
}
