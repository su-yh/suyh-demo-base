package com.web.sys.excel.export;

import com.base.mp.mybatis.PageParam;
import com.base.mp.mybatis.PageResult;
import com.web.ruoyi.excel.annotation.RuoyiExcel;
import com.web.ruoyi.excel.poi.RuoyiExcelUtil;
import com.web.sys.util.TestDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
//@ActiveProfiles("suyh")
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(
//        classes = CommunityEbusinessManager.class,
//        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ExcelExportTest {
    private static final int TOTAL_EXPORT_SIZE = 9;
    private static final int PAGE_SIZE = 1000;
    private static final int SHEET_SIZE = 500;

    @BeforeAll
    public static void setUpAll() {
    }

    @AfterAll
    public static void tearDownAll() {
    }

    @Test
    public void testExportTitleRuoyi() {
        PageParam pageParam = new PageParam();
        PageResult<ExportDto> pageResult = pageQuery(pageParam);
        List<ExportDto> list = pageResult.getList();

        RuoyiExcelUtil<ExportDto> excelExport = new RuoyiExcelUtil<>(ExportDto.class, Locale.CHINA);
        Path path = FileSystems.getDefault().getPath("D:/tmp/cem/excelExportTest.xlsx");
        try (OutputStream os = Files.newOutputStream(path)) {
            excelExport.init(list, "Sheet1", "", RuoyiExcel.Type.EXPORT);
            excelExport.exportExcel(os);
        } catch (IOException e) {
            log.error("failed", e);
        }
    }

    @Test
    public void testExportTitle() {
        ExcelExport<ExportDto> excelExport = new ExcelExport<>(ExportDto.class, Locale.CHINA, PAGE_SIZE, SHEET_SIZE);
        Path path = FileSystems.getDefault().getPath("D:/tmp/cem/excelExportTest.xlsx");
        try (OutputStream os = Files.newOutputStream(path)) {
            excelExport.pageExportStream(os, this::pageQuery);
        } catch (IOException e) {
            log.error("failed", e);
        }
    }

    private PageResult<ExportDto> pageQuery(PageParam pageParam) {
        log.info("pageNo: {}", pageParam.getPageNo());
        int curPageCount = PageParamUtilsTest.calculatePageSize(pageParam, TOTAL_EXPORT_SIZE);
        List<ExportDto> list = TestDataUtils.buildDtoList(curPageCount);
        return new PageResult<>(list, (long) TOTAL_EXPORT_SIZE);
    }

}