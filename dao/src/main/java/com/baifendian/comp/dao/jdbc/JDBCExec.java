package com.baifendian.comp.dao.jdbc;

import com.baifendian.comp.common.exception.BiException;
import com.baifendian.comp.common.structs.datasource.JDBCParam;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCExec {

  public static class TimeCount implements AutoCloseable {

    private long start = System.currentTimeMillis();
    private String sql;

    public TimeCount(String sql) {
      this.sql = sql;
      logger.info("Begin run sql:{}", sql);
    }

    @Override
    public void close() throws Exception {
      long stop = System.currentTimeMillis();
      if (stop - start > 300) {
        logger.info("Sql run time too long, time:{}, sql:{}", stop - start, sql);
      }
    }
  }

  private static final Logger logger = LoggerFactory.getLogger(JDBCExec.class);

  public static void dropTable(JDBCParam parameter, String tableName){
    String dropTable = "DROP table IF EXISTS " + tableName ;

    logger.info("drop table, sql:{}", dropTable);
    execute(parameter, dropTable);
  }

  public static void execute(JDBCParam parameter, String sql) {
    try (TimeCount ignored = new TimeCount(sql)) {
      try (DsConnect dsConnect = new DsConnect(parameter)) {
        dsConnect.execute(sql);
      } catch (BiException e) {
        throw e;
      } catch (Exception e) {
        logger.error("Datasource error.", e);
        throw new JdbcException(e);
      }
    }catch (BiException e){
      throw e;
    } catch (Exception e) {
      //
    }
  }

  public static List<List<Object>> dsExec(JDBCParam parameter, String sql) {
    try (TimeCount ignored = new TimeCount(sql)) {
      try (DsConnect dsConnect = new DsConnect(parameter)) {
        return dsConnect.getTableData(sql);
      } catch (BiException e) {
        throw e;
      } catch (Exception e) {
        logger.error("Datasource error.", e);
        throw new JdbcException(e);
      }
    } catch (Exception e) {
      //
      throw new BiException(e, "Time count error.");
    }
  }

  public static <T> T dsExec(JDBCParam parameter, ConnectCallback<T> callback) {
    try (DsConnect dsConnect = new DsConnect(parameter)) {
      return callback.call(dsConnect);
    } catch (BiException e) {
      throw e;
    } catch (Exception e) {
      logger.error("Datasource error.", e);
      throw new JdbcException(e);
    }
  }

  public static List<List<Object>> dsExec(JDBCParam parameter, String sql
      , ResultSetFunc function) {
    try (TimeCount ignored = new TimeCount(sql)) {
      try (DsConnect dsConnect = new DsConnect(parameter)) {
        return dsConnect.getTableData(sql, function);
      } catch (BiException e) {
        throw e;
      } catch (Exception e) {
        logger.error("Datasource error.", e);
        throw new JdbcException(e);
      }
    } catch (Exception e) {
      //
      throw new BiException(e, "Time count error.");
    }
  }

}
