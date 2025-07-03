package com.web.sys.service;

import com.web.ruoyi.mybatis.entity.SysDictData;
import com.web.ruoyi.mybatis.entity.SysDictType;
import com.web.ruoyi.mybatis.mapper.SysDictDataMapper;
import com.web.ruoyi.mybatis.mapper.SysDictTypeMapper;
import com.web.sys.vo.SysDict;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author suyh
 * @since 2025-07-01
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysDictService {
    private final SysDictTypeMapper sysDictTypeMapper;
    private final SysDictDataMapper sysDictDataMapper;

    private volatile List<SysDict> sysDictList;

    @NonNull
    public List<SysDict> obtainSysDictList() {
        if (sysDictList != null) {
            return sysDictList;
        }

        synchronized (this) {
            if (sysDictList == null) {
                initSysDictList();
            }
        }

        return sysDictList;
    }

    private void initSysDictList() {
        List<SysDictType> sysDictTypeList = sysDictTypeMapper.selectList();
        if (sysDictTypeList == null || sysDictTypeList.isEmpty()) {
            sysDictList = new ArrayList<>();
            return;
        }

        sysDictList = sysDictTypeList.stream().filter(Objects::nonNull).filter(SysDictType::isEnabled)
                .filter(t -> StringUtils.hasText(t.getDictType()))
                .peek(t -> t.setDictType(t.getDictType().trim()))
                .map(SysDict::new).collect(Collectors.toList());

        if (sysDictList.isEmpty()) {
            return;
        }

        List<SysDictData> sysDictDataList = sysDictDataMapper.selectList();
        if (sysDictDataList == null || sysDictDataList.isEmpty()) {
            return;
        }

        for (SysDictData sysDictData : sysDictDataList) {
            if (!sysDictData.isEnabled()) {
                continue;
            }

            // 处理空白字符串
            {
                String dictType = sysDictData.getDictType();
                if (!StringUtils.hasText(dictType)) {
                    continue;
                }
                dictType = dictType.trim();
                sysDictData.setDictType(dictType);
            }

            // 处理空白字符串
            {
                String dictValue = sysDictData.getDictValue();
                if (!StringUtils.hasText(dictValue)) {
                    continue;
                }
                dictValue = dictValue.trim();
                sysDictData.setDictValue(dictValue);
            }

            for (SysDict sysDict : sysDictList) {
                SysDictType sysDictType = sysDict.getDictType();
                if (sysDictType.getDictType().equals(sysDictData.getDictType())) {
                    sysDict.getDictDataList().add(sysDictData);
                }
            }
        }

        for (SysDict sysDict : sysDictList) {
            List<SysDictData> dictDataList = sysDict.getDictDataList();
            dictDataList.sort(Comparator.comparingInt(SysDictData::getDictSort));
        }
    }

}
