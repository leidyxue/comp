package com.baifendian.comp.dao.postgresql.mapper;

import com.baifendian.comp.common.enums.TableType;
import com.baifendian.comp.dao.postgresql.model.table.Table;
import com.baifendian.comp.dao.utils.EnumFieldUtil;
import com.baifendian.bi.engine.util.SqlUtil;
import com.google.common.base.Joiner;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.jdbc.SQL;

public class TableMapperProvider {

  final private static String TABLE_NAME = "\"table\"";

  /**
   * 插入
   */
  public String insert() {
    return new SQL() {
      {
        INSERT_INTO(TABLE_NAME);
        VALUES("id", "#{table.id}");
        VALUES("name", "#{table.name}");
        VALUES("origin_name", "#{table.originName}");
        VALUES("\"desc\"", "#{table.desc}");
        VALUES("type", EnumFieldUtil.genFieldStr("table.type", TableType.class, "table_type"));
        //VALUES("parameter", JsonFieldUtil.genFieldStr("table.parameter"));
        VALUES("create_time", "#{table.createTime}");
        VALUES("modify_time", "#{table.modifyTime}");
        VALUES("owner", "#{table.owner}");
        VALUES("ds_id", "#{table.dsId}");
        VALUES("parent_id", "#{table.parentId}");
      }
    }.toString();
  }

  @SuppressWarnings("unchecked")
  public String insertAll(Map<String, Object> parameter) {
    List<Table> flowNodes = (List<Table>) parameter.get("list");
    StringBuilder sb = new StringBuilder();
    sb.append("INSERT INTO ");
    sb.append(TABLE_NAME);
    sb.append(
        "(id, name, origin_name, " + SqlUtil.fieldAddQuote("desc")
            + ", type, create_time, modify_time, owner, ds_id, parent_id) ");
    sb.append("VALUES ");
    String fm =
        "(#'{'list[{0}].id}, #'{'list[{0}].name}, #'{'list[{0}].originName}, #'{'list[{0}].desc}, "
            //+"#'{'list[{0}].type,javaType=TableType,typeHandler=org.apache.ibatis.type.EnumTypeHandler}::table_type"
            + EnumFieldUtil.genEnumFieldStr("list[{0}].type", TableType.class, "table_type")
            + ", now(), now(), #'{'list[{0}].owner}, "
            + "#'{'list[{0}].dsId}, #'{'list[{0}].parentId})";
    MessageFormat mf = new MessageFormat(fm);
    for (int i = 0; i < flowNodes.size(); i++) {
      sb.append(mf.format(new Object[]{i}));
      if (i < flowNodes.size() - 1) {
        sb.append(",");
      }
    }
    return sb.toString();
  }

  public String getUserList() {
    return new SQL() {
      {
        SELECT("distinct \"owner\"");
        FROM(TABLE_NAME);
      }
    }.toString();
  }

  public String selectById() {
    return new SQL() {
      {
        SELECT("t.*, d.name ds_name, d.type ds_type, d.parameter jdbc_parameter");
        FROM("\"table\" t join \"datasource\" d on d.id = t.ds_id");
        WHERE("t.id = #{tId}");
      }
    }.toString();
  }

  public String findChartTableByUser() {
    return new SQL() {
      {
        SELECT("t.*, d.name ds_name, d.type ds_type, d.parameter jdbc_parameter");
        FROM("\"table\" t join \"datasource\" d on d.id = t.ds_id"+
        " join chart t3 on t.id = t3.table_id");
        WHERE("d.id = #{dsId}");
        WHERE("t.owner = #{userId}");
      }
    }.toString();
  }

  public String selectByIds(Map<String, Object> parameter) {
    Set<String> ids = (Set<String>) parameter.get("ids");

    return new SQL() {
      {
        SELECT("t.*, d.name ds_name, d.type ds_type, d.parameter jdbc_parameter");
        FROM("\"table\" t join \"datasource\" d on d.id = t.ds_id");
        WHERE("t.id in ('" + joiner.join(ids) + "')");
      }
    }.toString();
  }

  public String selectByUser() {
    return new SQL() {
      {
        SELECT("t.*, d.name ds_name, d.type ds_type, d.parameter jdbc_parameter");
        FROM("\"table\" t join \"datasource\" d on d.id = t.ds_id");
        WHERE("t.owner = #{userId} ");
        ORDER_BY("create_time");
      }
    }.toString();
  }

  public String selectByDs() {
    return new SQL() {
      {
        SELECT("t.*, d.name ds_name, d.type ds_type, d.parameter jdbc_parameter");
        FROM(SqlUtil.fieldAddQuote("table") + " t join " + SqlUtil.fieldAddQuote("datasource")
            + " d on d.id = t.ds_id");
        WHERE("t.ds_id = #{dsId}");
        WHERE("t.deleted = false");
        WHERE("t.owner = #{userId}");
      }
    }.toString();
  }

  public String countByFolder() {
    return new SQL() {
      {
        SELECT("count(1)");
        FROM(TABLE_NAME);
        WHERE("parent_id = #{parentId}");
      }
    }.toString();
  }

  public String selectChartDashByTid() {
    return new SQL() {
      {
        SELECT("t.*, d.name ds_name, d.type ds_type");
        FROM("\"table\" t join \"datasource\" d on d.id = t.ds_id");
      }
    }.toString();
  }

  public String selectNumByTId() {
    return new SQL() {
      {
        SELECT("count(1)");
        FROM("\"chart\" ");
        WHERE("table_id = #{tId}");
      }
    }.toString();
  }

  public String deleteByDsId() {
    return new SQL() {
      {
        DELETE_FROM(TABLE_NAME);
        WHERE("ds_id = #{dsId}");
        WHERE("owner = #{userId}");
      }
    }.toString();
  }

  public String deleteByTableId() {
    return new SQL() {
      {
        DELETE_FROM(TABLE_NAME);
        WHERE("id = #{tId}");
      }
    }.toString();
  }
  public String deleteByType() {
    return new SQL() {
      {
        DELETE_FROM(TABLE_NAME);
        WHERE("\"owner\" = #{userId}");
        WHERE("\"type\" = "+EnumFieldUtil.genFieldStr("type", TableType.class, "table_type"));
      }
    }.toString();
  }

  public String updateById() {
    return new SQL() {
      {
        UPDATE(TABLE_NAME);
        SET("name = #{table.name}");
        SET("origin_name = #{table.originName}");
        SET("\"desc\" = #{table.desc}");
        SET("\"type\" = " + EnumFieldUtil.genFieldStr("table.type", TableType.class, "table_type"));
        SET("\"modify_time\" = #{table.modifyTime}");
        SET("\"parent_id\" = #{table.parentId}");
        SET("\"ds_id\" = #{table.dsId}");
        WHERE("id = #{table.id}");
      }
    }.toString();
  }

  public String updateDeleteById(Map<String, Object> parameter) {
    return new SQL() {
      {
        UPDATE(TABLE_NAME);
        SET("deleted = true");
        WHERE("id = #{tableId}");
      }
    }.toString();
  }

  public String selectByType() {
    return new SQL() {
      {
        SELECT("t.*, d.name ds_name, d.type ds_type, d.parameter jdbc_parameter");
        FROM(TABLE_NAME + " t");
        JOIN("\"datasource\" d on d.id = t.ds_id");
        WHERE("t.type = "+EnumFieldUtil.genFieldStr("type", TableType.class, "table_type"));
        WHERE("t.owner = #{userId}");
      }
    }.toString();
  }

  public String selectByChartIds(Map<String, Object> parameter) {
    Set<String> chartIds = (Set<String>) parameter.get("chartIds");

    return new SQL() {
      {
        SELECT("t.*, d.name ds_name, d.type ds_type, d.parameter jdbc_parameter");
        FROM(TABLE_NAME + " t");
        JOIN("\"chart\" c on t.id = c.table_id");
        JOIN("\"datasource\" d on d.id = t.ds_id");
        WHERE("c.id in ('"+joiner.join(chartIds)+"')");
      }
    }.toString();
  }
  Joiner joiner = Joiner.on("','");
}
