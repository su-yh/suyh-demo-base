package com.web.sys.excel.handler.plus;

import com.web.sys.excel.handler.ExcelHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author suyh
 * @since 2025-05-15
 */
@Slf4j
public class ExcelCellHandlerDefault implements ExcelHandlerAdapter {
    public static final ExcelCellHandlerDefault instance = new ExcelCellHandlerDefault();

    @Override
    public void serializable(Workbook wb, Cell cell, MessageSource messageSource, Locale locale, Object cellObj, String argsJson) {
        if (cellObj == null) {
            return;
        }
        cell.setCellValue(cellObj.toString());
    }
}
