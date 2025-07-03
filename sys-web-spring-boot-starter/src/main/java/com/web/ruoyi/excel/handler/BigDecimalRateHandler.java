package com.web.ruoyi.excel.handler;

import com.web.ruoyi.excel.poi.RuoyiExcelHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * @author suyh
 * @since 2024-09-20
 */
@Slf4j
public class BigDecimalRateHandler implements RuoyiExcelHandlerAdapter {
    @Override
    public Object format(Object value, Locale locale, String[] args, Cell cell, Workbook wb) {
        if (value == null) {
            return null;
        }
        Class<?> bigDecimalClassType = value.getClass();
        if (!BigDecimal.class.isAssignableFrom(bigDecimalClassType)) {
            log.error("NON {}, UNKNOWN CLASS: {}", BigDecimal.class.getSimpleName(), value.getClass().getSimpleName());

            return null;
        }

        return new DecimalFormat("0.00%").format(value);
    }
}
