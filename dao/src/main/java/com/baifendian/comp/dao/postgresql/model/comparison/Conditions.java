package com.baifendian.comp.dao.postgresql.model.comparison;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2017/11/17 0017.
 */
@Builder
@Setter
@Getter
public class Conditions {
  List<Condition> conditions;
}
