package com.seepine.http;

import com.seepine.http.exception.HttpException;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okhttp3.internal.http.StatusLine;

import java.io.IOException;
import java.io.InputStream;
/**
 * @author seepine
 */
public class Response {
  private final okhttp3.Response response;
  private final ResponseBody body;
  private final boolean isOk;
  private final boolean isRedirect;
  private final int status;
  private String bodyStr;

  public Response(okhttp3.Response response) {
    this.response = response;
    this.body = response.body();
    isOk = response.isSuccessful();
    isRedirect = response.isRedirect();
    status = response.code();
  }

  public Headers headers() {
    return response.headers();
  }

  public okhttp3.Response getResponse() {
    return response;
  }

  public StatusLine getStatusLine() {
    return StatusLine.Companion.get(response);
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
        throw new HttpException(e);
      }
    }
    return bodyStr;
  }

  public byte[] bodyBytes() {
    try {
      return body.bytes();
    } catch (IOException e) {
      throw new HttpException(e);
    }
  }

  public InputStream bodyStream() {
    return body.byteStream();
  }
}
