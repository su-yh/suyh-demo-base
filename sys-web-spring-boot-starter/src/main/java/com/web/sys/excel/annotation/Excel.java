package com.web.sys.excel.annotation;

import com.web.sys.excel.args.HandlerArgs;
import com.web.sys.excel.handler.ExcelHandlerAdapter;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {
    /**
     * 导出时在excel中排序
     */
    int sort() default Integer.MAX_VALUE;

    /**
     * 导出到Excel中的名字.
     * 首行标题
     */
    String name() default "";

    /**
     * 国际化对应的code
     */
    String nameCode() default "";

    /**
     * 导出列头背景颜色
     */
    IndexedColors headerBackgroundColor() default IndexedColors.GREY_50_PERCENT;

    /**
     * 导出列头字体颜色
     */
    IndexedColors headerColor() default IndexedColors.WHITE;

    /**
     * 是否使用父元素的style 应用到标题头
     */
    boolean useParentHeaderStyle() default true;

    /**
     * 自定义数据处理器
     */
    Class<? extends ExcelHandlerAdapter> handler() default ExcelHandlerAdapter.class;

    /**
     * json 格式的参数
     *
     * @see HandlerArgs
     */
    String argsJson() default "";

//    /**
//     * 字段类型（0：导出导入；1：仅导出；2：仅导入）
//     */
//    Type type() default Type.ALL;

    /**
     * 当前字段的类型，或者List 中的T 是否作为最小解析单元处理
     */
    boolean minParseUnit() default true;
//
//    enum Type {
//        ALL(0), EXPORT(1), IMPORT(2);
//        private final int value;
//
//        Type(int value) {
//            this.value = value;
//        }
//
//        public int value() {
//            return this.value;
//        }
//    }
}