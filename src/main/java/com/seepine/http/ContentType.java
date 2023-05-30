package com.seepine.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import okhttp3.MediaType;

/**
 * @author seepine
 */
public enum ContentType {
  /**
   * 标准表单编码，当action为get时候，浏览器用x-www-form-urlencoded的编码方式把form数据转换成一个字串（name1=value1&amp;name2=value2…）
   */
  FORM_URLENCODED("application/x-www-form-urlencoded"),
  /** 文件上传编码，浏览器会把整个表单以控件为单位分割，并为每个部分加上Content-Disposition，并加上分割符(boundary) */
  MULTIPART("multipart/form-data"),
  /** Rest请求JSON编码 */
  JSON("application/json"),
  /** Rest请求XML编码 */
  XML("application/xml"),
  /** text/plain编码 */
  TEXT_PLAIN("text/plain"),
  /** Rest请求text/xml编码 */
  TEXT_XML("text/xml"),
  /** text/html编码 */
  TEXT_HTML("text/html"),
  /** application/octet-stream编码 */
  OCTET_STREAM("application/octet-stream");

  private final String value;

  /**
   * 构造
   *
   * @param value ContentType值
   */
  ContentType(String value) {
    this.value = value;
  }

  /**
   * 输出Content-Type字符串，附带编码信息
   *
   * @param contentType Content-Type类型
   * @param charset 编码
   * @return Content-Type字符串
   */
  public static String parse(String contentType, Charset charset) {
    return String.format("%s;charset=%s", contentType, charset.name());
  }

  /**
   * 输出Content-Type字符串，附带编码信息
   *
   * @param contentType Content-Type 枚举类型
   * @param charset 编码
   * @return Content-Type字符串
   */
  public static String parse(ContentType contentType, Charset charset) {
    return parse(contentType.getValue(), charset);
  }

  public static ContentType parse(MediaType mediaType) {
    if (mediaType == null) {
      return null;
    }
    String contentType = mediaType.type() + "/" + mediaType.subtype();
    for (ContentType item : ContentType.values()) {
      if (contentType.startsWith(item.getValue())) {
        return item;
      }
    }
    return null;
  }

  /**
   * 获取value值
   *
   * @return value值
   * @since 5.2.6
   */
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return getValue();
  }

  /**
   * 输出Content-Type字符串，附带编码信息
   *
   * @param charset 编码
   * @return Content-Type字符串
   */
  public String toString(Charset charset) {
    return parse(this, charset);
  }

  /**
   * 输出toMediaType
   *
   * @return MediaType
   */
  public MediaType toMediaType() {
    return MediaType.parse(ContentType.parse(value, StandardCharsets.UTF_8));
  }

  /**
   * 输出toMediaType
   *
   * @param charset charset
   * @return MediaType
   */
  public MediaType toMediaType(Charset charset) {
    return MediaType.parse(ContentType.parse(value, charset));
  }

  public boolean equals(MediaType mediaType) {
    return this.equals(ContentType.parse(mediaType));
  }
}
