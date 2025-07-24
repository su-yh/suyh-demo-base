package com.web.sys.excel.export;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.mp.mybatis.PageParam;
import com.base.mp.mybatis.PageResult;
import com.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.base.web.exception.ExceptionUtil;
import com.web.sys.authentication.user.LoginUser;
import com.web.sys.excel.annotation.Excel;
import com.web.sys.excel.handler.ExcelHandlerAdapter;
import com.web.sys.excel.util.ExcelUtils;
import com.web.sys.excel.vo.FieldDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.context.MessageSource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * Excel相关处理
 */
@Slf4j
public class ExcelExport<T> {
    public static MessageSource MESSAGE_SOURCE;

    /**
     * 分页导出：每次查询一页的数量
     */
    private int pageSize = ExcelUtils.PAGE_SIZE;

    /**
     * Excel 单sheet最大行数的大概值，并没有绝对准确
     */
    public int sheetSize = ExcelUtils.SHEET_SIZE;

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 当前页码：从1 开始. 0 为无效页
     */
    private int currentSheetNum = 0;

    // 数据单元格的格式全部处理成一样的
    private CellStyle dataCellStyle;

    /**
     * 注解列表（树型结构）：所有属性的字段以及对应的注解
     */
    @Getter
    private List<FieldDetail> fieldDetailList;

    // 标题一共占用几行
    private int titleRowCount = 0;

    // 每一列的列宽，0 表示未配置，使用初始化
    private int[] columnWidthArrays;

    private int maxColumnIndex = -1;

    /**
     * 统计列表
     */
    private final Map<Integer, Double> statistics = new HashMap<>();

    /**
     * 实体对象
     */
    private final Class<T> clazz;

    private final List<SFunction<?, ?>[]> exclude = new ArrayList<>();

    private final Locale locale;

    // 是否合并单元格
    @Setter
    private boolean mergeCell = true;

    public ExcelExport(Class<T> clazz, Locale locale) {
        this(clazz, locale, ExcelUtils.PAGE_SIZE, ExcelUtils.SHEET_SIZE);
    }

    public ExcelExport(Class<T> clazz, Locale locale, int pageSize, int sheetSize) {
        this.pageSize = pageSize;
        this.sheetSize = sheetSize;
        this.clazz = clazz;
        this.locale = locale;
    }

    private void init() {
        initFieldDetail();

        this.wb = new SXSSFWorkbook(500);

        // 初始化一个页
        obtainAvailableSheet();
    }

    public void listExport(
            LoginUser loginUser, String exportDesc, String queryParamsJson,
            HttpServletResponse response, List<T> dataList) {
        this.pageSize = Integer.MAX_VALUE;

        Function<PageParam, PageResult<T>> pageQuery = pageParam -> {
            long total = dataList != null ? dataList.size() : 0L;
            return new PageResult<>(dataList, total);
        };

        pageExport(loginUser, exportDesc, queryParamsJson, response, pageQuery);
    }

    public void pageExport(
            LoginUser loginUser, String exportDesc, String queryParamsJson,
            HttpServletResponse response, Function<PageParam, PageResult<T>> pageQuery) {
        log.info("\n[{}], user id: {}, user nickname: {}, \nquery params: \n{}",
                exportDesc, loginUser.getId(), loginUser.getNickname(), queryParamsJson);
        pageExport(response, pageQuery);
    }

    /**
     * 分页导出，每次查询一页数据，写入excel 之后再查询下一页的数据。
     */
    private void pageExport(
            HttpServletResponse response, Function<PageParam, PageResult<T>> pageQuery) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        try {
            ServletOutputStream outputStream = response.getOutputStream();
            write(pageQuery);
            wb.write(outputStream);
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        } finally {
            IOUtils.closeQuietly(wb);
        }
    }

    public void pageExportStream(OutputStream stream, Function<PageParam, PageResult<T>> pageQuery) {
        try {
            write(pageQuery);
            wb.write(stream);
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        } finally {
            IOUtils.closeQuietly(wb);
        }
    }

    private void write(Function<PageParam, PageResult<T>> pageQuery) throws InstantiationException, IllegalAccessException {
        this.init();

        PageParam pageParam = new PageParam();
        pageParam.setPageSize(pageSize);
        int total = 0;
        int currentSize = 0;

        do {
            PageResult<T> pageResult = pageQuery.apply(pageParam);
            // 第一页，使用分页查询接口
            if (pageParam.isSearchCount()) {
                // 只有查询了总数的那一次的总数值才是可信的。
                total = pageResult.getTotal().intValue();
                pageParam.setSearchCount(false); // 总数，只查一次
            }

            List<T> entityList = pageResult.getList();
            int size = entityList != null ? entityList.size() : 0;
            currentSize += size;

            BigDecimal percentage = total > 0
                    ? BigDecimal.valueOf(currentSize).divide(BigDecimal.valueOf(total), 4, RoundingMode.HALF_DOWN)
                    : BigDecimal.ZERO;
            log.info("excel page export, current page num: {}, page size: {}, size: {}/{}({})",
                    pageParam.getPageNo(), pageParam.getPageSize(), currentSize, total, new DecimalFormat("0.00%").format(percentage));

            this.writeExcelData(entityList);

            Integer pageNo = pageParam.getPageNo();
            pageParam.setPageNo(pageNo + 1);    // 下一页
        } while (currentSize < total);

        log.info("excel page export finished, total size: {}", total);
    }

    private Sheet obtainAvailableSheet() {
        if (sheet != null && sheet.getLastRowNum() < sheetSize + titleRowCount - 1) {
            return sheet;
        }

        this.sheet = wb.createSheet();
        this.currentSheetNum++;
        wb.setSheetName(this.currentSheetNum - 1, "Sheet" + this.currentSheetNum);

        createSheetTitle();

        initColumnWidth();

        titleRowCount = sheet.getLastRowNum() + 1;
        return sheet;
    }

    private void initColumnWidth() {
        if (columnWidthArrays != null) {
            return;
        }

        columnWidthArrays = new int[maxColumnIndex + 1];
        Arrays.fill(columnWidthArrays, 0);

        initColumnWidth(fieldDetailList);

        for (int i = 0; i < columnWidthArrays.length; i++) {
            int width = columnWidthArrays[i];
            if (width <= 0) {
                width = 16;
            }

            sheet.setColumnWidth(i, width * 256);
        }
    }

    private void initColumnWidth(List<FieldDetail> fieldDetailList) {
        if (fieldDetailList == null || fieldDetailList.isEmpty()) {
            return;
        }

        for (FieldDetail fieldDetail : fieldDetailList) {
            if (!fieldDetail.isExportFlag()) {
                continue;
            }
            boolean minParseUnit = fieldDetail.isMinParseUnit();
            if (minParseUnit) {
                Excel anno = fieldDetail.getAnno();
                Integer colIndex = fieldDetail.getColIndex();
                int width = anno.colWidth();
                // 列宽取最大值
                if (columnWidthArrays[colIndex] < width) {
                    columnWidthArrays[colIndex] = width;
                }
            } else {
                List<FieldDetail> childList = fieldDetail.getChildList();
                initColumnWidth(childList);
            }
        }
    }

    protected void createSheetTitle() {
        Row row = this.sheet.createRow(0);
        createSheetTitle(this.fieldDetailList, this.sheet, row, 0, null);
    }

    /**
     * 递归实现创建标题，递归实现就不支持树型结构的数据
     *
     * @param row 当前行
     * @param col 列下标
     * @return 返回创建的标题占了几列
     */
    protected int createSheetTitle(
            @NonNull List<FieldDetail> fieldDetailList, @NonNull Sheet sheet,
            @NonNull Row row, int col, @Nullable CellStyle parentTitleStyle) {
        int totalCol = 0;
        int rowNum = row.getRowNum();
        int colNum = col;
        int nextRowNum = rowNum + 1;
        // 需要合并的单元格
        List<Cell> mergeList = new ArrayList<>();
        boolean multiRowFlag = false;
        for (FieldDetail fieldDetail : fieldDetailList) {
            if (!fieldDetail.isExportFlag()) {
                continue;
            }
            Excel anno = fieldDetail.getAnno();

            Cell rowTitleCell = row.createCell(colNum);

            String title = MESSAGE_SOURCE != null
                    ? MESSAGE_SOURCE.getMessage(anno.nameCode(), null, anno.name(), locale)
                    : anno.name();
            rowTitleCell.setCellValue(title);

            CellStyle titleStyle = createTitleCellStyle(anno);
            if (parentTitleStyle != null && anno.useParentHeaderStyle()) { // 使用父元素相同的背景色与文字颜色
                // 父元素
                Font parentFont = wb.getFontAt(parentTitleStyle.getFontIndexAsInt());

                // 字体
                Font curFont = wb.getFontAt(titleStyle.getFontIndexAsInt());
                curFont.setColor(parentFont.getColor());

                // 背景色
                titleStyle.setFillForegroundColor(parentTitleStyle.getFillForegroundColor());
            }

            rowTitleCell.setCellStyle(titleStyle);

            List<FieldDetail> childList = fieldDetail.getChildList();
            if (childList != null && !childList.isEmpty()) {
                Row nextRow = sheet.getRow(nextRowNum);
                if (nextRow == null) {
                    nextRow = sheet.createRow(nextRowNum);
                }
                // 子集(child)的第一列需要与上一个对象的列对齐
                int childCol = createSheetTitle(childList, sheet, nextRow, colNum, titleStyle);
                colNum += childCol;
                totalCol += childCol;

                if (childCol > 1) {
                    // 合并单元格
                    int columnIndex = rowTitleCell.getColumnIndex();
                    CellRangeAddress cellAddress = new CellRangeAddress(rowNum, rowNum, columnIndex, columnIndex + childCol - 1);
                    sheet.addMergedRegion(cellAddress);
                }

                multiRowFlag = true;
            } else {
                colNum++;
                totalCol++;

                mergeList.add(rowTitleCell);
            }
        }

        final int maxTitleRowNum = sheet.getLastRowNum();
        if (multiRowFlag && maxTitleRowNum - rowNum > 0) {
            if (mergeCell) {
                // 合并单元格，合并同一列的多行数据
                for (Cell cell : mergeList) {
                    int columnIndex = cell.getColumnIndex();
                    CellRangeAddress cellAddress = new CellRangeAddress(rowNum, maxTitleRowNum, columnIndex, columnIndex);
                    sheet.addMergedRegion(cellAddress);
                }
            } else {
                // TODO: suyh - 如果不合并单元格，则对应的列在不同的行中应该有相同的值
                throw ExceptionUtil.business(BaseWebErrorCodeEnums.NO_IMPLEMENT);
            }
        }

        return totalCol;
    }

    private void writeExcelData(List<T> list) throws IllegalAccessException, InstantiationException {
        if (list == null || list.isEmpty()) {
            return;
        }

        for (T obj : list) {
            Sheet sheet = obtainAvailableSheet();
            int lastRowNum = sheet.getLastRowNum();
            List<Cell> mergeCellList = new ArrayList<>();
            int rowCount = fillExcelRowData(sheet, lastRowNum + 1, obj, fieldDetailList, mergeCellList);
            if (rowCount > 1) {
                // 合并单元格
                for (Cell cell : mergeCellList) {
                    int rowIndex = cell.getRowIndex();
                    int columnIndex = cell.getColumnIndex();
                    CellRangeAddress cellAddress = new CellRangeAddress(rowIndex, rowIndex + rowCount - 1, columnIndex, columnIndex);
                    sheet.addMergedRegion(cellAddress);
                }
            }
        }
    }

    /**
     * @param sheet           excel 对应的页
     * @param startRowNum     已被使用的行号的下一行，即该行还未创建。
     * @param obj             用于填充这一行的数据对象
     * @param fieldDetailList 该数据对象对应的字段注解解析列表
     * @param mergeCellList   填充需要被合并的单元格，若为null 则不需要填充
     * @return 当前对象填充完一共占用了几行
     */
    protected int fillExcelRowData(
            @NonNull Sheet sheet, int startRowNum,
            @Nullable Object obj, List<FieldDetail> fieldDetailList,
            @Nullable List<Cell> mergeCellList)
            throws IllegalAccessException, InstantiationException {
        log.debug("fillExcelData, row: {}", startRowNum);

        if (obj == null) {
            return 0;
        }

        if (fieldDetailList == null || fieldDetailList.isEmpty()) {
            log.error("rowNum: {}, Class<{}>, fieldDetailList is null or empty.", startRowNum, obj.getClass().getSimpleName());
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        }

        int rowCount = 1;
        for (FieldDetail fieldDetail : fieldDetailList) {
            if (!fieldDetail.isExportFlag()) {
                continue;
            }
            Field field = fieldDetail.getField();

            field.setAccessible(true);
            Object fieldValue = field.get(obj);
            Class<?> fieldType = field.getType();
            if (Map.class.isAssignableFrom(fieldType)) {
                log.error("暂时不支持Map 类型");
                throw ExceptionUtil.business(BaseWebErrorCodeEnums.SYSTEM_UNSUPPORTED, "Map type parsing is not supported");
            } else if (List.class.isAssignableFrom(fieldType)) {
                int curElementRowCount = 0;
                // 集合类型的序列化
                List<?> elementArray = (List<?>) fieldValue;
                if (elementArray == null) {
                    elementArray = new ArrayList<>();
                }
                for (Object element : elementArray) {
                    // 递归
                    int fillRowCount = fillExcelCellData(fieldDetail, element, sheet, startRowNum + curElementRowCount, null);
                    curElementRowCount += fillRowCount;
                }

                if (curElementRowCount > rowCount) {
                    rowCount = curElementRowCount;
                }
            } else if (fieldType.isArray()) {
                int curElementRowCount = 0;
                int length = fieldValue == null ? 0 : Array.getLength(fieldValue);
                for (int i = 0; i < length; i++) {
                    Object element = Array.get(fieldValue, i);
                    // 递归
                    int fillRowCount = fillExcelCellData(fieldDetail, element, sheet, startRowNum + curElementRowCount, null);
                    curElementRowCount += fillRowCount;
                }

                if (curElementRowCount > rowCount) {
                    rowCount = curElementRowCount;
                }
            } else {
                // 递归
                int fillRowCount = fillExcelCellData(fieldDetail, fieldValue, sheet, startRowNum, mergeCellList);
                if (fillRowCount > rowCount) {
                    rowCount = fillRowCount;
                }
            }
        }

        return rowCount;
    }


    protected int fillExcelCellData(
            FieldDetail fieldDetail, @Nullable final Object element,
            @NonNull Sheet sheet, final int currentRowNum,
            @Nullable List<Cell> mergeCellList)
            throws IllegalAccessException, InstantiationException {
        Excel excelAnn = fieldDetail.getAnno();
        List<FieldDetail> childList = fieldDetail.getChildList();
        ExcelHandlerAdapter adapterInstance = fieldDetail.getAdapterInstance();

        int rowCount = 1;
        if (childList != null && !childList.isEmpty()) {
            // 复合数据类型。递归处理
            rowCount = fillExcelRowData(sheet, currentRowNum, element, childList, mergeCellList);
        } else {
            Row childRow = sheet.getRow(currentRowNum);
            if (childRow == null) {
                childRow = sheet.createRow(currentRowNum);
            }
            CellStyle cellStyle = obtainDataCellStyle();
            Cell cell = childRow.createCell(fieldDetail.getColIndex());
            cell.setCellStyle(cellStyle);
            adapterInstance.serializable(wb, cell, MESSAGE_SOURCE, locale, element, excelAnn.argsJson());

            if (mergeCellList != null) {
                mergeCellList.add(cell);
            }
        }

        return rowCount;
    }

    private CellStyle obtainDataCellStyle() {
        if (dataCellStyle == null) {
            dataCellStyle = wb.createCellStyle();
            dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
            dataCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Font dataFont = wb.createFont();
            dataFont.setFontName("Arial");
            dataFont.setFontHeightInPoints((short) 10);
            dataCellStyle.setFont(dataFont);

            DataFormat dataFormat = wb.createDataFormat();
            dataCellStyle.setDataFormat(dataFormat.getFormat("0"));
        }

        return dataCellStyle;
    }

    @NonNull
    private CellStyle createTitleCellStyle(Excel excel) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(excel.headerBackgroundColor().index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(excel.headerColor().index);
        style.setFont(headerFont);
        // 设置表格头单元格文本形式
        DataFormat dataFormat = wb.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("@"));

        return style;
    }

    private void initFieldDetail() {
        if (fieldDetailList != null) {
            return;
        }

        this.fieldDetailList = ExcelUtils.parseFieldDetail(clazz);

        for (SFunction<?, ?>[] sf : exclude) {
            doExcludeField(this.fieldDetailList, sf, 0);
        }

        ExcelUtils.orderFieldList(this.fieldDetailList, 0);

        maxColumnIndex = maxValidColumnIndex(this.fieldDetailList, -1);
        if (maxColumnIndex < 0) {
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        }
    }

    private int maxValidColumnIndex(List<FieldDetail> fieldDetailList, int maxIndex) {
        if (fieldDetailList == null || fieldDetailList.isEmpty()) {
            return maxIndex;
        }

        for (FieldDetail fieldDetail : fieldDetailList) {
            if (!fieldDetail.isExportFlag()) {
                continue;
            }

            if (fieldDetail.isMinParseUnit()) {
                maxIndex = Math.max(maxIndex, fieldDetail.getColIndex());
            } else {
                List<FieldDetail> childList = fieldDetail.getChildList();
                maxIndex = maxValidColumnIndex(childList, maxIndex);
            }
        }

        return maxIndex;
    }

    /**
     * 一次添加一个要导出的字段，从第一层开始，如果有多层，则一个一个往下写。
     * <pre><code>
     * excelExport.addExcludeField(
     *         (SFunction<ExportDto, ?>) ExportDto::getDetail,
     *         (SFunction<ExportDetailDto, ?>) ExportDetailDto::getDetailDto02,
     *         (SFunction<ExportDetailDto02, ?>) ExportDetailDto02::getDto03,
     *         (SFunction<ExportDetailDto03, ?>) ExportDetailDto03::getExportField
     * );
     * </code></pre>
     */
    public void addExcludeField(SFunction<?, ?>... fields) {
        exclude.add(fields);
    }

    private void doExcludeField(List<FieldDetail> details, SFunction<?, ?>[] fields, int index) {
        if (fields == null || fields.length == 0) {
            return;
        }
        if (details == null || details.isEmpty()) {
            log.error("exclude field failed, details is empty");
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        }

        SFunction<?, ?> field = fields[index];
        LambdaMeta meta = LambdaUtils.extract(field);
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());

        FieldDetail detailMatch = null;
        for (FieldDetail detail : details) {
            if (detail.getField().getName().equals(fieldName)) {
                detailMatch = detail;
                break;
            }
        }

        if (detailMatch == null) {
            log.error("exclude field failed, field MISMATCH. class: {}, fieldName: {}",
                    meta.getInstantiatedClass().getName(), fieldName);
            throw ExceptionUtil.business(BaseWebErrorCodeEnums.SERVICE_ERROR);
        }

        // System.out.printf("fieldName: %s, className: %s%n", fieldName, detailMatch.getFieldClass());

        int nextIndex = index + 1;
        if (nextIndex >= fields.length) {
            // 找到了要排除的属性，将其标记为排除
            detailMatch.setExportFlag(false);
            return;
        }

        doExcludeField(detailMatch.getChildList(), fields, index + 1);
    }
}
