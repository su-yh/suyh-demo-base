package com.web.sys.excel.export;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.base.mp.mybatis.PageParam;
import com.base.mp.mybatis.PageResult;
import com.example.SimpleExampleApplication;
import com.web.sys.util.TestDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * @author suyh
 * @since 2025-06-19
 */
@ActiveProfiles("suyh")
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = SimpleExampleApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Slf4j
public class ExcelExportTest {
    private static final int TOTAL_EXPORT_SIZE = 9;
    private static final int PAGE_SIZE = 1000;
    private static final int SHEET_SIZE = 500;

    @Resource
    private MessageSource messageSource;

    @BeforeAll
    public static void setUpAll() {
    }

    @AfterAll
    public static void tearDownAll() {
    }

    @Test
    public void testExportTitle() {
        ExcelExport.MESSAGE_SOURCE = messageSource;

        ExcelExport<ExportDto> excelExport = new ExcelExport<>(ExportDto.class, Locale.CHINA, PAGE_SIZE, SHEET_SIZE);
        Path path = FileSystems.getDefault().getPath("E:/tmp/cem/excelExportTest.xlsx");
        try (OutputStream os = Files.newOutputStream(path)) {
            excelExport.pageExportStream(os, this::pageQuery);
        } catch (IOException e) {
            log.error("failed", e);
            Assertions.fail();
        }
    }

    private PageResult<ExportDto> pageQuery(PageParam pageParam) {
        log.info("pageNo: {}", pageParam.getPageNo());
        int curPageCount = PageParamUtilsTest.calculatePageSize(pageParam, TOTAL_EXPORT_SIZE);
        List<ExportDto> list = TestDataUtils.buildDtoList(curPageCount);
        return new PageResult<>(list, (long) TOTAL_EXPORT_SIZE);
    }


    @Test
    public void testColumn() {
//        SFunction<ExportDto, ?> column = ExportDto::getDetail;
//        LambdaMeta meta = LambdaUtils.extract(column);
//        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
//        Class<?> instantiatedClass = meta.getInstantiatedClass();
//        System.out.printf("fieldName: %s, instantiatedClass: %s", fieldName, instantiatedClass.getName());

//        excludeField(ExportDto::getDetail, ExportDetailDto::getUuid);

//        List<FieldDetail> details = new ArrayList<>();
        ExcelExport<ExportDto> excelExport = new ExcelExport<>(ExportDto.class, Locale.CHINA, PAGE_SIZE, SHEET_SIZE);

        excelExport.addExcludeField(
                (SFunction<ExportDto, ?>) ExportDto::getDetail,
                (SFunction<ExportDetailDto, ?>) ExportDetailDto::getDetailDto02,
                (SFunction<ExportDetailDto02, ?>) ExportDetailDto02::getDto03,
                (SFunction<ExportDetailDto03, ?>) ExportDetailDto03::getExportField
        );
    }
//
//    private void excludeField(List<FieldDetail> details, SFunction<?, ?>... fields) {
//        doExcludeField(details, fields, 0);
//    }
//
//    private void doExcludeField(List<FieldDetail> details, SFunction<?, ?>[] fields, int index) {
//        if (fields == null || fields.length == 0) {
//            return;
//        }
//        if (details == null || details.isEmpty()) {
//            throw new RuntimeException("details is empty");
//        }
//
//        SFunction<?, ?> field = fields[index];
//        LambdaMeta meta = LambdaUtils.extract(field);
//        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
////        Class<?> instantiatedClass = meta.getInstantiatedClass();
//
//        FieldDetail detailMatch = null;
//        for (FieldDetail detail : details) {
//            if (detail.getField().getName().equals(fieldName)) {
//                detailMatch = detail;
//                break;
//            }
//        }
//
//        if (detailMatch == null) {
//            throw new RuntimeException("MISMATCH");
//        }
//
//        System.out.printf("fieldName: %s, className: %s%n", fieldName, detailMatch.getFieldClass());
//
//        int nextIndex = index + 1;
//        if (nextIndex >= fields.length) {
//            // 找到了要排除的属性，将其标记为排除
//            detailMatch.setExportFlag(false);
//            return;
//        }
//
//        doExcludeField(detailMatch.getChildList(), fields, index + 1);
//    }
}