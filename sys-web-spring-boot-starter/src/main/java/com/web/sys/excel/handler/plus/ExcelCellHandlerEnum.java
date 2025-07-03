package com.web.sys.excel.handler.plus;

import com.web.ruoyi.excel.handler.ExcelEnumValueHandler;
import com.web.sys.excel.handler.ExcelHandlerAdapter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Locale;

/**
 * @author suyh
 * @since 2025-06-23
 */
public class ExcelCellHandlerEnum extends ExcelEnumValueHandler implements ExcelHandlerAdapter {
    @Override
    public void serializable(Workbook wb, Cell cell, Locale locale, Object cellObj, String argsJson) {
        Object cellValue = format(cellObj, locale, null, cell, wb);
        if (cellValue == null) {
            return;
        }

        cell.setCellValue(cellValue.toString());
    }
}
