package com.web.sys.excel.export;

import com.ebusiness.constant.enums.OrderAuditStatusEnums;
import com.ebusiness.enums.TransferStatusTestEnums;
import com.ebusiness.rouyi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private OrderAuditStatusEnums auditStatus;
    @JsonIgnore
    private TransferStatusTestEnums statusTest;

    @Excel(sort = 3, name = "detailDto02", minParseUnit = false, useParentHeaderStyle = false, headerBackgroundColor = IndexedColors.LIGHT_YELLOW, headerColor = IndexedColors.BLACK, colWidth = 200)
    private ExportDetailDto02 detailDto02;
}
