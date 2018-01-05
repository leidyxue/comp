package com.baifendian.comp.common.structs.chart;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

@Setter
@Getter
public class DrillDownLevel {
  private List<ChartFieldParam> x = new ArrayList<>();
  private List<ChartFieldParam> y = new ArrayList<>();
  private List<ChartFieldParam> yOptional = new ArrayList<>();
  private JsonNode styleConf;

  public boolean hasX(){
    return CollectionUtils.isNotEmpty(x);
  }

  public boolean hasY(){
    return CollectionUtils.isNotEmpty(y);
  }

  public boolean hasYOpt(){
    return CollectionUtils.isNotEmpty(yOptional);
  }

  public DrillDownLevel sort(){
    if (x != null){
      x = x.stream().sorted(Comparator.comparing(ChartFieldParam::getOrder)).collect(Collectors.toList());
    }
    if (y != null){
      y = y.stream().sorted(Comparator.comparing(ChartFieldParam::getOrder))
          .collect(Collectors.toList());
    }
    if (yOptional != null){
      yOptional = yOptional.stream().sorted(Comparator.comparing(ChartFieldParam::getOrder)).collect(
          Collectors.toList());
    }
    return this;
  }

  public static void main(String[] args) {
    IntStream.range(0,0).forEach(System.out::println);
    //System.out.println(IntStream.range(0,1));
  }
}
