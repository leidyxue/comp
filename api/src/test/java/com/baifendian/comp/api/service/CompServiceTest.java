package com.baifendian.comp.api.service;

import com.baifendian.comp.api.RestfulApiApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RestfulApiApplication.class)
public class CompServiceTest {


  @Autowired
  private ComparisonService comparisonService;

  @Test
  public void exec() {
    String taskId = "task_82b812eb653824c61071e755242ef42d";
    comparisonService.exec("590090585582574419", taskId);
  }

  @Test
  public void onlineTest() {
    String taskId = "task_172539fddb52e26111c3373648548629";
    //comparisonService.onlineTest(taskId);
  }

  @Test
  public void preview() {
    String resultId = "result_b8dd5e630871e5d4d56fa2294b2e3788";
    comparisonService.previewResult("590090585582574419", resultId);
  }

  @Test
  public void deleteResult() {
    String resultId = "result_MieuW5v5";
    comparisonService.deleteResult(resultId, resultId);
  }

}