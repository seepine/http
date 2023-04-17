package com.seepine.http.exception;

/**
 * 自定义Runtime异常类
 *
 * @author seepine
 * @since 1.2.0
 */
public class HttpException extends RuntimeException {

  public HttpException(String msg) {
    super(msg);
  }

  /**
   * 使用指定的详细信息消息和原因构造新的运行时异常。
   *
   * @param e 异常
   */
  public HttpException(Throwable e) {
    super(e.getMessage(), e);
  }

  /**
   * 使用指定的详细信息消息和原因构造新的运行时异常。
   *
   * @param msg 错误信息
   * @param e 异常
   */
  public HttpException(String msg, Throwable e) {
    super(msg, e);
  }
}
