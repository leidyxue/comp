package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.dao.postgresql.model.chart.ChartField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
public class DrillDownData {

  private List<ChartField> x = new ArrayList<>();
  private List<ChartField> y = new ArrayList<>();
  private List<ChartField> yOptional = new ArrayList<>();
  private List<String> drills = new ArrayList<>();

  public void addDrills(List<ChartField> fields){
    for (ChartField cf: fields){
      drills.add(cf.getFieldId());
    }
  }

  public void addChartFields(List<ChartField> fields){
    for (ChartField cf: fields){
      switch (cf.getType()){
        case X:
          x.add(cf);
          break;

        case Y:
          y.add(cf);
          break;

        case Y_OPT:
          yOptional.add(cf);
          break;

//          default:
//            drills.add(cf.getFieldId());

      }
    }
  }

  public String getFieldId(String id){
    if (CollectionUtils.isNotEmpty(x)){
      Optional<ChartField> field = x.stream().filter(f -> StringUtils.equals(id, f.getUniqId()))
          .findFirst();
      if (field.isPresent()){
        return field.get().getFieldId();
      }
    }

    if (CollectionUtils.isNotEmpty(y)){
      Optional<ChartField> field = y.stream().filter(f -> StringUtils.equals(id, f.getUniqId()))
          .findFirst();
      if (field.isPresent()){
        return field.get().getFieldId();
      }
    }

    if (CollectionUtils.isNotEmpty(yOptional)){
      Optional<ChartField> field = yOptional.stream().filter(f -> StringUtils.equals(id, f.getUniqId()))
          .findFirst();
      if (field.isPresent()){
        return field.get().getFieldId();
      }
    }

    return "";
  }
}
