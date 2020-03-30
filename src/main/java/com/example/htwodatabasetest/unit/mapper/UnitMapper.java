package com.example.htwodatabasetest.unit.mapper;

import com.easy.mybatis.common.mapper.BaseMapper;
import com.example.htwodatabasetest.unit.model.UnitPO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("unitMapper")
public interface UnitMapper extends BaseMapper<UnitPO> {
    List<UnitPO> select1();
}