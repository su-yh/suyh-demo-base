package com.web.sys.excel.vo;

import com.web.sys.excel.annotation.Excel;
import com.web.sys.excel.handler.ExcelHandlerAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author suyh
 * @since 2025-06-19
 */
@RequiredArgsConstructor
@Getter
public class FieldDetail {
    private final Field field;
    private final Excel anno;

    /**
     * 有几种情况：
     * 1. 该字段是基础数据类型
     * 2. 该字段是复合数据类型，即：class
     * 3. 该字段是List 集合或者数组类型
     * 3.1 List或者数组 中的模板元素类型是基础数据类型
     * 3.2 List或者数组 中的模板元素类型是复合数据类型
     * <p>
     * 所以，首先应该判断是否为List 或者数组
     *
     * 如果该字段是最小解析单元，才会有值，否则为null
     */
    private final ExcelHandlerAdapter adapterInstance;

    // 非最小解析单元时非null
    private final List<FieldDetail> childList;

    private final Class<?> fieldClass;

    @Setter
    private Integer colIndex;   // 该元素所在列，如果列有合并，则该值为最左列

    public boolean isMinParseUnit() {
        return anno.minParseUnit();
    }
}
