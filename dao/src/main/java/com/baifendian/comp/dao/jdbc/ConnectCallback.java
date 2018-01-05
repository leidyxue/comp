package com.baifendian.comp.dao.jdbc;

public interface ConnectCallback<T> {
  T call(DsConnect dsConnect);
}
