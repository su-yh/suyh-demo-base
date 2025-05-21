package com.example.business.mapper.mysql.business;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.base.mp.mybatis.BaseMapperX;
import com.base.mp.mybatis.LambdaQueryWrapperX;
import com.base.mp.mybatis.PageParam;
import com.base.mp.mybatis.PageResult;
import com.example.business.entity.mysql.business.ExampleTestEntity;
import com.example.constant.DataSourceNames;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author suyh
 * @since 2024-09-02
 */
@Mapper
@DS(DataSourceNames.MASTER)
public interface ExampleTestMapper extends BaseMapperX<ExampleTestEntity> {
    default PageResult<ExampleTestEntity> pageList(PageParam pageParam) {
        LambdaQueryWrapperX<ExampleTestEntity> queryWrapperX = build();

        return selectPage(pageParam, queryWrapperX);
    }
}
