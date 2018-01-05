package com.baifendian.comp.common.exception;

/**
 * Created by shuanghu
 * data: 17-4-24.
 */
public class LocalFileException extends BiException {

  public LocalFileException(Throwable cause, String msgKey) {
    super(cause, msgKey);
  }

  public LocalFileException(String msgKey) {
    super(msgKey);
  }

  public LocalFileException(String msg, String msgKey) {
    super(msg, msgKey, null);
  }

  public static LocalFileException buildMkdirException(String fileName) {
    return new LocalFileException("File mkdir failed, name:" + fileName,
        "com.baifendian.bi.common.localFile.mkdirError");
  }

  public static LocalFileException buildSaveException() {
    return new LocalFileException("", "com.baifendian.bi.common.localFile.mkdirError");
  }

  public static LocalFileException buildFileNotFoundException() {
    return new LocalFileException("", "com.baifendian.bi.common.localFile.mkdirError");
  }

  public static LocalFileException buildIOException() {
    return new LocalFileException("", "com.baifendian.bi.common.localFile.mkdirError");
  }
}
