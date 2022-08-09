package com.seepine.http;

import com.seepine.http.entity.DownloadProgressListener;
import com.seepine.http.entity.ProgressListener;
import com.seepine.http.entity.ProgressMultipartRequestBody;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
/**
 * @author seepine
 */
public class Request {
  private final String url;
  private Method method = Method.GET;
  private ContentType contentType = ContentType.FORM_URLENCODED;
  private final Charset charset = StandardCharsets.UTF_8;
  private final Headers.Builder headers =
      new Headers.Builder()
          .add(
              "user-agent",
              "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36 Edg/99.0.1150.55");
  /** 存储url数据 */
  private final Map<String, Object> params = new HashMap<>();
  /** 存储表单数据 */
  FormBody.Builder formBodyBuild = new FormBody.Builder();
  /** 储存请求体 */
  private okhttp3.RequestBody requestBody;

  private String okHttpClientName = HttpClientPool.DEFAULT_CLIENT_KEY;

  private Request(String url) {
    this.url = url;
  }

  public static Request url(String url, Method method) {
    return new Request(url).method(method);
  }

  public static Request get(String url) {
    return Request.url(url, Method.GET);
  }

  public static Request post(String url) {
    return Request.url(url, Method.POST);
  }

  public static Request put(String url) {
    return Request.url(url, Method.PUT);
  }

  public static Request delete(String url) {
    return Request.url(url, Method.DELETE);
  }

  public Request httpClientName(String okHttpClientName) {
    this.okHttpClientName = okHttpClientName;
    return this;
  }

  private Request method(Method method) {
    this.method = method;
    return this;
  }

  public Request addParam(String name, Object value) {
    if (name == null || "".equals(name) || value == null) {
      return this;
    }
    params.put(name, value);
    return this;
  }

  public Request addForm(String name, Object value) {
    if (name == null || "".equals(name) || value == null) {
      return this;
    }
    if (method.equals(Method.GET)) {
      params.put(name, value);
    } else {
      formBodyBuild.add(name, value.toString());
    }
    return this;
  }

  public Request addHeader(String name, String value) {
    headers.add(name, value);
    return this;
  }

  public Request body(String jsonBody) {
    contentType = ContentType.JSON;
    requestBody = RequestBody.create(MediaType.parse(contentType.toString(charset)), jsonBody);
    return this;
  }

  public Request body(File file) {
    contentType = ContentType.MULTIPART;
    requestBody =
        new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                file.getName(),
                RequestBody.create(MediaType.parse(ContentType.MULTIPART.getValue()), file))
            .build();
    return this;
  }

  public Request body(File file, ProgressListener progressListener) {
    contentType = ContentType.MULTIPART;
    requestBody =
        new ProgressMultipartRequestBody.Builder()
            .addProgressListener(progressListener)
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                file.getName(),
                RequestBody.create(MediaType.parse(ContentType.MULTIPART.getValue()), file))
            .build();
    return this;
  }

  public Request body(RequestBody requestBody) {
    this.requestBody = requestBody;
    return this;
  }

  private okhttp3.Request buildOkHttpRequest() {
    okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
    builder.url(url());
    switch (this.method) {
      case POST:
        builder.post(buildRequestBody());
        break;
      case PUT:
        builder.put(buildRequestBody());
        break;
      case DELETE:
        builder.delete(buildRequestBody());
        break;
      case PATCH:
        builder.patch(buildRequestBody());
        break;
      case HEAD:
        builder.head();
        break;
      default:
        builder.get();
    }
    builder.headers(headers.build());
    return builder.build();
  }

  public String url() {
    String newUrl = this.url;
    if (!params.isEmpty()) {
      newUrl += "?";
      AtomicReference<String> str = new AtomicReference<>("");
      params.forEach(
          (name, value) -> {
            String enVal;
            try {
              enVal = URLEncoder.encode(value.toString(), StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
              enVal = value.toString();
            }
            str.getAndSet(str.get() + "&" + name + "=" + enVal);
          });
      newUrl += str.get().substring(1);
    }
    return newUrl;
  }

  private RequestBody buildRequestBody() {
    if (requestBody != null) {
      return requestBody;
    }
    if (formBodyBuild != null) {
      return formBodyBuild.build();
    }
    return new FormBody.Builder().build();
  }

  public Response execute() {
    try {
      return new Response(
          HttpClientPool.get(okHttpClientName).newCall(buildOkHttpRequest()).execute());
    } catch (IOException e) {
      return new Response(e);
    }
  }

  /**
   * 带进度的请求，一般用于大文件下载
   *
   * @param progressListener 进度实现方法
   * @return 请求结果
   */
  public Response execute(DownloadProgressListener progressListener) {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    final Response[] res = new Response[1];
    HttpClientPool.get(okHttpClientName)
        .newCall(buildOkHttpRequest())
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                res[0] = new Response(e);
                countDownLatch.countDown();
              }

              @Override
              public void onResponse(Call call, okhttp3.Response response) {
                try {
                  InputStream is = Objects.requireNonNull(response.body()).byteStream();
                  byte[] buf = new byte[8192];
                  int len;
                  long sum = 0;
                  long total = Objects.requireNonNull(response.body()).contentLength();
                  while ((len = is.read(buf)) != -1) {
                    sum += len;
                    progressListener.onProgress(buf, len, sum, total);
                  }
                  is.close();
                  res[0] = new Response(response);
                } catch (Exception e) {
                  res[0] = new Response(response, e);
                } finally {
                  countDownLatch.countDown();
                }
              }
            });
    try {
      countDownLatch.await();
      return res[0];
    } catch (Exception e) {
      return new Response(e);
    }
  }

  public void execute(Callback callback) {
    HttpClientPool.get(okHttpClientName).newCall(buildOkHttpRequest()).enqueue(callback);
  }
}
