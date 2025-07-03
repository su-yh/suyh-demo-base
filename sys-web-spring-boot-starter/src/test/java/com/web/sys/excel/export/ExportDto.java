package com.web.sys.excel.export;

import com.web.ruoyi.excel.annotation.RuoyiExcel;
import com.web.ruoyi.excel.handler.BigDecimalRateHandler;
import com.web.ruoyi.excel.handler.ExcelEnumValueHandler;
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
    @Excel(sort = 1, name = "dateTime")
    @RuoyiExcel(sort = 1, name = "dateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date dateTime = new Date();
    @Excel(sort = 3, name = "amount", argsJson = HandlerArgsBigDecimal.JSON_DECIMAL_FORMAT_FIXED_TWO)
    @RuoyiExcel(sort = 3, name = "amount", handler = BigDecimalRateHandler.class)
    private BigDecimal amount;
    @Excel(sort = 3, name = "uuidList")
    private List<String> uuidList;
    @Excel(sort = 7, name = "ids")
    private List<Long> ids;
    @Excel(sort = 8, name = "rateList", argsJson = HandlerArgsBigDecimal.JSON_DECIMAL_FORMAT_PERCENTAGE)
    private List<BigDecimal> rateList;
    @Excel(sort = 9, name = "statusTest")
    @RuoyiExcel(sort = 9, name = "statusTest", handler = ExcelEnumValueHandler.class)
    private TransferStatusTestEnums statusTest;

    @Excel(sort = 2, name = "detail", headerBackgroundColor = IndexedColors.SKY_BLUE, minParseUnit = false)
    private ExportDetailDto detail;

    @Excel(sort = 300, name = "detail2", headerBackgroundColor = IndexedColors.SKY_BLUE, minParseUnit = false)
    private ExportDetailDto detail2;

    @Excel(sort = 5, name = "detailDtoList", headerBackgroundColor = IndexedColors.SKY_BLUE, minParseUnit = false)
    private List<ExportDetailDto> detailDtoList;
}
