package com.baifendian.comp.api.service;

import com.baifendian.comp.api.RestfulApiApplication;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import com.baifendian.comp.common.enums.DSType;
import com.baifendian.comp.common.structs.datasource.Datasource;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RestfulApiApplication.class)
public class DatasourceServiceTest {
  @Autowired
  private DatasourceService datasourceService;

  @Test
  public void testCreateDatasource(){
    Datasource datasource = new Datasource();
    JDBCParam JDBCParam = new JDBCParam();
    JDBCParam.setAddress("119.23.22.38:3306");
    JDBCParam.setDatabase("bi");
    JDBCParam.setUser("admin");
    JDBCParam.setPassword("123456");
    datasource.setName("test11");
    datasource.setOwner("user_0b83572b-93ee-4033-8545-ab4277dc873c");
    datasource.setCreateTime(new Date());
    datasource.setModifyTime(new Date());
    datasource.setType(DSType.MYSQL);
    datasource.setParameter(JDBCParam);
    datasourceService.createDS(datasource);
  }
}
