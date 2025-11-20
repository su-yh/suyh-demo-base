package com.web.sys.excel.export;

import com.web.sys.excel.annotation.Excel;
import com.web.sys.excel.args.HandlerArgsBigDecimal;
import lombok.Data;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author suyh
 * @since 2025-06-19
 */
@Data
public class ExportDto extends ExportParentDto {
    @Excel(sort = 1, name = "dateTime", colWidth = 20)
    private Date dateTime = new Date();
    @Excel(sort = 3, name = "amount", argsJson = HandlerArgsBigDecimal.JSON_DECIMAL_FORMAT_FIXED_TWO)
    private BigDecimal amount;
    @Excel(sort = 3, name = "uuidList")
    private List<String> uuidList;
    @Excel(sort = 7, name = "ids")
    private List<Long> ids;
    @Excel(sort = 8, name = "rateList", argsJson = HandlerArgsBigDecimal.JSON_DECIMAL_FORMAT_PERCENTAGE)
    private List<BigDecimal> rateList;

    @Excel(sort = 2, name = "detail", minParseUnit = false, headerBackgroundColor = IndexedColors.GREEN, colWidth = 100)
    private ExportDetailDto detail;

    @Excel(sort = 300, name = "detail2", minParseUnit = false, headerBackgroundColor = IndexedColors.SKY_BLUE)
    private ExportDetailDto detail2;

    @Excel(sort = 5, name = "detailDtoList", minParseUnit = false, headerBackgroundColor = IndexedColors.ROSE)
    private List<ExportDetailDto> detailDtoList;
}
