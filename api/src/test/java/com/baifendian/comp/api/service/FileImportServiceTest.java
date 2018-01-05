package com.baifendian.comp.api.service;

import com.baifendian.comp.api.RestfulApiApplication;
import com.baifendian.comp.api.dto.fileImport.TableHead;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RestfulApiApplication.class)
public class FileImportServiceTest {

  @Autowired
  private FileImportService fileImportService;

  @Test
  public void createTableField() throws Exception {
    String userId = "u123";

    List<String> data = new ArrayList<>();
    data.add("1,a,123.4");
    data.add("23,3,123");
    data.add("2,we2,123");
    data.add("1q1,dd,11");
    data.add("11,32,ss");
    data.add("11,32,ss");
//    FileSplitData fileSplitData = fileImportService.splitData(",", data);
    System.out.print("OK");
  }

  @Test
  public void fileImportTest() {
    String userId = "u123";

    new TableHead();
    List<TableHead> tableHeadList = new ArrayList<>();
//    tableHeadList.add(new TableHead("id", FieldTypeInfo.NUM));
//    tableHeadList.add(new TableHead("name", FieldTypeInfo.TEXT));
//    fileImportService.fileImport(userId, "tacc", tableHeadList, null, "tac", ",");
  }

}