package com.web.sys.vo;

import com.web.ruoyi.mybatis.entity.SysDictData;
import com.web.ruoyi.mybatis.entity.SysDictType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author suyh
 * @since 2025-07-01
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SysDict {
    private SysDictType dictType;
    private final List<SysDictData> dictDataList = new ArrayList<>();
}
