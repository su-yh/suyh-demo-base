package com.web.sys.excel.util;

import com.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.base.web.exception.ExceptionUtil;
import com.web.sys.excel.annotation.Excel;
import com.web.sys.excel.handler.ExcelHandlerAdapter;
import com.web.sys.excel.handler.plus.ExcelCellHandlerBigDecimal;
import com.web.sys.excel.handler.plus.ExcelCellHandlerDate;
import com.web.sys.excel.handler.plus.ExcelCellHandlerDefault;
import com.web.sys.excel.handler.plus.ExcelCellHandlerEnum;
import com.web.sys.excel.handler.plus.ExcelCellHandlerInteger;
import com.web.sys.excel.handler.plus.ExcelCellHandlerLong;
import com.web.sys.excel.handler.plus.ExcelCellHandlerString;
import com.web.sys.excel.vo.FieldDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author suyh
 * @since 2025-05-15
 */
@Slf4j
public class ExcelUtils {
    /**
     * 分页导出时，一次查询的记录数量。
     */
    public static int PAGE_SIZE = 10000;

    /**
     * Excel 单sheet最大行数的大概值，并没有绝对准确
     */
    public static int SHEET_SIZE = 50000;

    protected static final Map<Class<?>, ExcelHandlerAdapter> HANDLER_ADAPTER_MAP = new HashMap<>();

    static {
        // 初始化基本类型的适配器
        ExcelCellHandlerDate excelCellHandlerDate = new ExcelCellHandlerDate();
        ExcelCellHandlerInteger excelCellHandlerInteger = new ExcelCellHandlerInteger();
        ExcelCellHandlerLong excelCellHandlerLong = new ExcelCellHandlerLong();
        ExcelCellHandlerString excelCellHandlerString = new ExcelCellHandlerString();
        ExcelCellHandlerBigDecimal excelCellHandlerBigDecimal = new ExcelCellHandlerBigDecimal();
        ExcelCellHandlerEnum excelEnumValueHandler = new ExcelCellHandlerEnum();

        HANDLER_ADAPTER_MAP.put(Date.class, excelCellHandlerDate);
        HANDLER_ADAPTER_MAP.put(int.class, excelCellHandlerInteger);
        HANDLER_ADAPTER_MAP.put(Integer.class, excelCellHandlerInteger);
        HANDLER_ADAPTER_MAP.put(long.class, excelCellHandlerLong);
        HANDLER_ADAPTER_MAP.put(Long.class, excelCellHandlerLong);
        HANDLER_ADAPTER_MAP.put(String.class, excelCellHandlerString);
        HANDLER_ADAPTER_MAP.put(BigDecimal.class, excelCellHandlerBigDecimal);
        HANDLER_ADAPTER_MAP.put(Enum.class, excelEnumValueHandler);

        // 其他适配器，继续添加
    }

    /**
     * 获取字段注解信息
     */
    @NonNull
    public static List<FieldDetail> parseFieldDetail(Class<?> cls) {
        List<FieldDetail> fieldDetailList = new ArrayList<>();
        List<Field> fieldList = allFields(cls);
        for (Field field : fieldList) {
            // 排除静态属性
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            Excel excelAnn = field.getAnnotation(Excel.class);
            if (excelAnn == null) {
                continue;
            }

            // 属性的Class，或者List 的模板Class
            Class<?> fieldClass = obtainFieldClass(field);

            List<FieldDetail> childList = null;
            ExcelHandlerAdapter adapterInstance = null;
            boolean minParseUnit = excelAnn.minParseUnit();
            if (minParseUnit) {
                Class<? extends ExcelHandlerAdapter> handlerClazz = excelAnn.handler();
                if (handlerClazz.equals(ExcelHandlerAdapter.class)) { // 使用默认的处理器
                    Class<?> mapKey = fieldClass;
                    if (Enum.class.isAssignableFrom(fieldClass)) {
                        mapKey = Enum.class;
                    }
                    adapterInstance = HANDLER_ADAPTER_MAP.get(mapKey);
                    if (adapterInstance == null) {
                        log.debug("field handler not found, Class: {}, use default", fieldClass.getSimpleName());
                        adapterInstance = ExcelCellHandlerDefault.instance;
                    }
                } else {
                    try {
                        adapterInstance = handlerClazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        log.error("instance excel handle adapter failed, class: {}", handlerClazz.getSimpleName());
                        throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
                    }
                }
            } else {
                childList = parseFieldDetail(fieldClass);
            }

            FieldDetail fieldDetail = new FieldDetail(field, excelAnn, adapterInstance, childList, fieldClass);
            fieldDetailList.add(fieldDetail);
        }
        return fieldDetailList;
    }

    @NonNull
    public static List<Field> allFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            Field[] declaredFields = clazz.getDeclaredFields();
            allFields.addAll(Arrays.asList(declaredFields));
            clazz = clazz.getSuperclass();
        }
        return allFields;
    }


    protected static Class<?> obtainFieldClass(Field field) {
        if (Map.class.isAssignableFrom(field.getType())) {
            log.error("暂时不支持Map 类型");
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SYSTEM_UNSUPPORTED, "Map type parsing is not supported");
        }

        if (Collection.class.isAssignableFrom(field.getType())) {
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            return (Class<?>) pt.getActualTypeArguments()[0];
        }

        if (field.getType().isArray()) {
            return field.getType().getComponentType();
        }

        return field.getType();
    }

    // 字段排序
    public static void orderFieldList(List<FieldDetail> fieldDetailList, final int col) {
        if (fieldDetailList == null || fieldDetailList.isEmpty()) {
            return;
        }

        fieldDetailList.sort(Comparator.comparing(vo -> vo.getAnno().sort()));

        int nextCol = col;
        for (FieldDetail fieldDetail : fieldDetailList) {
            if (!fieldDetail.isExportFlag()) {
                continue;
            }
            // 在排序表头的时候直接确定好字段所在列，在进行数据处理的时候就会更方便。
            fieldDetail.setColIndex(nextCol);

            List<FieldDetail> childList = fieldDetail.getChildList();
            orderFieldList(childList, nextCol);  // 递归
            int incrementColNum = 1;
            if (childList != null && !childList.isEmpty()) {
                incrementColNum = childList.size();
            }

            nextCol += incrementColNum;
        }
    }
}
