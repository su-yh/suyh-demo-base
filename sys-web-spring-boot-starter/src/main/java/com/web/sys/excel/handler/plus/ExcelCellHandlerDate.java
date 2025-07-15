package com.web.sys.excel.handler.plus;

import com.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.base.web.exception.ExceptionUtil;
import com.base.web.util.JsonUtils;
import com.web.sys.excel.args.HandlerArgsDate;
import com.web.sys.excel.handler.ExcelHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author suyh
 * @since 2025-05-13
 */
@Slf4j
public class ExcelCellHandlerDate implements ExcelHandlerAdapter {
    private static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void serializable(Workbook wb, Cell cell, MessageSource messageSource, Locale locale, Object cellObj, String argsJson) {
        if (cellObj == null) {
            return;
        }

        String dateFormat = DATE_FORMAT_DEFAULT;
        if (StringUtils.hasText(argsJson)) {
            HandlerArgsDate handlerArgsDate = JsonUtils.deserialize(argsJson, HandlerArgsDate.class);
            if (handlerArgsDate == null) {
                log.error("parse args failed, class: {}, json: {}", HandlerArgsDate.class.getSimpleName(), argsJson);
                throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
            }

            if (StringUtils.hasText(handlerArgsDate.getDateFormat())) {
                dateFormat = handlerArgsDate.getDateFormat();
            }
        }

        Date date = (Date) cellObj;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String dateFmt = sdf.format(date);
        cell.setCellValue(dateFmt);
    }
}
