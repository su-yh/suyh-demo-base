package com.web.sys.excel.handler;

import com.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.base.web.exception.ExceptionUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.lang.Nullable;

import java.util.Locale;

/**
 * @author suyh
 * @since 2025-06-20
 */
public interface ExcelHandlerAdapter {
    default void serializable(Workbook wb, Cell cell, Locale locale, @Nullable Object cellObj, String argsJson) {
        System.out.println("Error: " + this.getClass().getSimpleName());
        throw ExceptionUtil.business(BaseWebErrorCodeEnums.NO_IMPLEMENT);
    }
}
