package com.web.sys.util;

import com.web.sys.excel.export.ExportDetailDto;
import com.web.sys.excel.export.ExportDto;
import com.web.sys.excel.export.TransferStatusTestEnums;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author suyh
 * @since 2025-06-10
 */
public class TestDataUtils {
    public final static Random RANDOM = new Random();
    public final static EnumsRandom<TransferStatusTestEnums> TRANSFER_STATUS_TEST_ENUMS_ENUMS_RANDOM = new EnumsRandom<>(TransferStatusTestEnums.values());

    private static final AtomicLong baseIdNumber = new AtomicLong(0);

    static {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime baseTime = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime nowTime = LocalDateTime.now();
        long epochMilliBase = baseTime.atZone(zoneId).toInstant().toEpochMilli();
        long epochMilliNow = nowTime.atZone(zoneId).toInstant().toEpochMilli();
        long baseNumberMilli = epochMilliNow - epochMilliBase;
        baseIdNumber.set(baseNumberMilli / 100);
    }

    /**
     * 生成一定宽度的十六进制字符，同0 填充不足宽度
     */
    public static String randomUniqueNumber() {
        long curIdNumber = baseIdNumber.getAndIncrement();
        return String.format("%012x", curIdNumber);
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static BigDecimal randomBigDecimal() {
        double doubleValue = RANDOM.nextDouble() * 10000.0;
        return BigDecimal.valueOf(doubleValue);
    }

    public static List<ExportDto> buildDtoList(Integer size) {
        if (size <= 0) {
            return null;
        }
        List<ExportDto> dtoList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ExportDto dto = buildExportDto(i + 1L);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public static ExportDto buildExportDto(Long id) {
        ExportDto dto = new ExportDto();
        dto.setId(id);
        dto.setUuid(TestDataUtils.uuid());
        dto.setAmount(TestDataUtils.randomBigDecimal());

        int uuidListSize = TestDataUtils.RANDOM.nextInt(5);
        List<String> uuidList = uuidListSize > 0 ? new ArrayList<>(uuidListSize) : null;
        for (int i = 0; i < uuidListSize; i++) {
            uuidList.add(TestDataUtils.uuid());
        }
        dto.setUuidList(uuidList);

        int idsSize = TestDataUtils.RANDOM.nextInt(5);
        List<Long> ids = idsSize > 0 ? new ArrayList<>(idsSize) : null;
        for (int i = 0; i < idsSize; i++) {
            ids.add(TestDataUtils.RANDOM.nextLong());
        }
        dto.setIds(ids);

        int amountListSize = TestDataUtils.RANDOM.nextInt(5);
        List<BigDecimal> amountList = amountListSize > 0 ? new ArrayList<>(amountListSize) : null;
        for (int i = 0; i < amountListSize; i++) {
            amountList.add(TestDataUtils.randomBigDecimal());
        }
        dto.setRateList(amountList);

        ExportDetailDto detailDto = buildExportDetailDto();
        dto.setDetail(detailDto);
        dto.setDetail2(detailDto);

        TransferStatusTestEnums transferStatusTest = TRANSFER_STATUS_TEST_ENUMS_ENUMS_RANDOM.obtainEnum();
        dto.setStatusTest(transferStatusTest);

        return dto;
    }

    private static ExportDetailDto buildExportDetailDto() {
        int id = TestDataUtils.RANDOM.nextInt(500);
        ExportDetailDto detailDto = new ExportDetailDto();
        detailDto.setId((long) id);
        detailDto.setUuid(TestDataUtils.uuid());
        return detailDto;
    }
}
