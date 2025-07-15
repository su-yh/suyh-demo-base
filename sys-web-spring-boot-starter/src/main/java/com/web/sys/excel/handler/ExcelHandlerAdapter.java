package com.web.sys.excel.handler;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;

import java.util.Locale;

/**
 * @author suyh
 * @since 2025-06-20
 */
public interface ExcelHandlerAdapter {
    void serializable(Workbook wb, Cell cell, MessageSource messageSource, Locale locale, @Nullable Object cellObj, String argsJson);
}
