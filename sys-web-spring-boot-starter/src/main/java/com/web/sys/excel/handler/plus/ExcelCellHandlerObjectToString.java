package com.web.sys.excel.handler.plus;//package com.ebusiness.rouyi.excel.handler.plus;
//
//import com.ebusiness.rouyi.excel.handler.ExcelHandlerAdapter;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.springframework.context.MessageSource;
//
//import java.util.Locale;
//
///**
// * @author suyh
// * @since 2025-05-13
// */
//public class ExcelCellHandlerObjectToString implements ExcelHandlerAdapter {
//    @Override
//    public void serializable(Workbook wb, Cell cell, MessageSource messageSource, Locale locale, Object cellObj, String argsJson) {
//        if (cellObj == null) {
//            return;
//        }
//        cell.setCellValue(cellObj.toString());
//    }
//}
