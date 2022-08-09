package com.seepine.http;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
/**
 * @author seepine
 */
public class Response {
  private okhttp3.Response response;
  private ResponseBody body;
  private boolean isOk = false;
  private boolean isRedirect = false;
  private int status = -1;

  private String bodyStr;

  public Response(okhttp3.Response response) {
    this.response = response;
    this.body = response.body();
    isOk = response.isSuccessful();
    isRedirect = response.isRedirect();
    status = response.code();
  }

  public Response(Exception e) {
    e.printStackTrace();
  }

  public Response(okhttp3.Response response, Exception e) {
    this.response = response;
    e.printStackTrace();
  }

  public Headers headers() {
    return response.headers();
  }

  /**
   * auto handle name
   *
   * <p>example [Server-Timing,server-Timing,server-timing] is same
   *
   * @param name name eg:location
   * @return headerValue
   */
  public String header(String name) {
    return response.header(name.trim());
  }

  public MediaType contentType() {
    return body.contentType();
  }

  public Long contentLength() {
    return body.contentLength();
  }

  public int status() {
    return status;
  }

  public boolean isRedirect() {
    return isRedirect;
  }

  public boolean isOk() {
    return isOk;
  }

  public ResponseBody body() {
    return body;
  }

  public String bodyStr() {
    if (bodyStr == null) {
      try {
        bodyStr = body.string();
      } catch (NullPointerException ignored) {
      } catch (IOException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }
    return bodyStr;
  }

  public byte[] bodyBytes() {
    try {
      return body.bytes();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public InputStream bodyStream() {
    return body.byteStream();
  }
}
