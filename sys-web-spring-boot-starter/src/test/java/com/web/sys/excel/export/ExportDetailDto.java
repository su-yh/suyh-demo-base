package com.web.sys.excel.export;

import com.web.sys.excel.annotation.Excel;
import lombok.Data;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * @author suyh
 * @since 2025-06-19
 */
@Data
public class ExportDetailDto {
    @Excel(sort = 3, name = "id", useParentHeaderStyle = false, headerBackgroundColor = IndexedColors.RED)
    private Long id;
    @Excel(sort = 3, name = "uuid", useParentHeaderStyle = false, headerBackgroundColor = IndexedColors.BLACK)
    private String uuid;

    @Excel(sort = 3, name = "detailDto02", minParseUnit = false, useParentHeaderStyle = false, headerBackgroundColor = IndexedColors.LIGHT_YELLOW, headerColor = IndexedColors.BLACK, colWidth = 200)
    private ExportDetailDto02 detailDto02;
}
