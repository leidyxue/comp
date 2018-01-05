package com.baifendian.comp.dao;

import com.baifendian.comp.dao.postgresql.MyBatisSqlSessionFactoryUtil;
import com.baifendian.comp.dao.postgresql.mapper.comparison.ResultMapper;
import com.baifendian.comp.dao.postgresql.model.comparison.ComparisonResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/11/26 0026.
 */
public class TaskDao extends BaseDao {

  @Autowired
  ResultMapper resultMapper;

  @Override
  protected void init(){
    resultMapper = MyBatisSqlSessionFactoryUtil.getSqlSession()
        .getMapper(ResultMapper.class);
  }

  public void getResult(String id){
    ComparisonResult comparisonResult = resultMapper.findById(id);
    System.out.println(comparisonResult.getStatus());
  }
}
