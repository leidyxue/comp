package com.baifendian.comp.api.service;

import com.baifendian.comp.api.RestfulApiApplication;
import com.baifendian.comp.dao.postgresql.mapper.TableMapperNew;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RestfulApiApplication.class)
public class TableServiceTest {


  @Autowired
  private TableMapperNew tableMapper;


  public static void main(String[] args) {
    List<String> strList = new ArrayList<>();
    strList.add("1");
    strList.add("2");
    strList.add("3");

    System.out.println(strList.stream().collect(Collectors.joining(", ")));
  }
}