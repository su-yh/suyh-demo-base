package com.web.sys.excel.handler.plus;

import com.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.base.web.exception.ExceptionUtil;
import com.base.web.util.JsonUtils;
import com.web.sys.excel.args.HandlerArgsBigDecimal;
import com.web.sys.excel.handler.ExcelHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * @author suyh
 * @since 2024-09-20
 */
@Slf4j
public class ExcelCellHandlerBigDecimal implements ExcelHandlerAdapter {
    @Override
    public void serializable(Workbook wb, Cell cell, Locale locale, Object cellObj, String argsJson) {
        if (cellObj == null) {
            return;
        }
        Class<?> bigDecimalClassType = cellObj.getClass();
        if (!BigDecimal.class.isAssignableFrom(bigDecimalClassType)) {
            log.error("NON {}, UNKNOWN CLASS: {}", BigDecimal.class.getSimpleName(), cellObj.getClass().getSimpleName());

            return;
        }

        if (StringUtils.hasText(argsJson)) {
            HandlerArgsBigDecimal handlerArgsBigDecimal = JsonUtils.deserialize(argsJson, HandlerArgsBigDecimal.class);
            if (handlerArgsBigDecimal == null) {
                log.error("parse args failed, class: {}, json: {}", HandlerArgsBigDecimal.class.getSimpleName(), argsJson);
                throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
            }

            String decimalFormatPattern = handlerArgsBigDecimal.getDecimalFormatPattern();
            String cellValue = StringUtils.hasText(decimalFormatPattern)
                    ? new DecimalFormat(decimalFormatPattern).format(cellObj)
                    : cellObj.toString();
            cell.setCellValue(cellValue);
        } else {
            cell.setCellValue(cellObj.toString());
        }
    }
}
