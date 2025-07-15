package com.web.sys.excel.handler.plus;

import com.web.ruoyi.excel.handler.ExcelEnumValueHandler;
import com.web.sys.excel.handler.ExcelHandlerAdapter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author suyh
 * @since 2025-06-23
 */
public class ExcelCellHandlerEnum extends ExcelEnumValueHandler implements ExcelHandlerAdapter {
    @Override
    public void serializable(Workbook wb, Cell cell, MessageSource messageSource, Locale locale, Object cellObj, String argsJson) {
        Object cellValue = format(cellObj, messageSource, locale, null, cell, wb);
        if (cellValue == null) {
            return;
        }

        cell.setCellValue(cellValue.toString());
    }
}
