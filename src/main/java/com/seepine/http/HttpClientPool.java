package com.seepine.http;

import okhttp3.OkHttpClient;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
/**
 * @author seepine
 */
public class HttpClientPool {
  private static volatile HttpClientPool http;
  public static final String DEFAULT_CLIENT_KEY = "com.seepine.http.default";
  private final Map<String, OkHttpClient> map = new HashMap<>();

  private HttpClientPool() {
    map.put(DEFAULT_CLIENT_KEY, defaultBuilt().build());
  }

  private static HttpClientPool getInstance() {
    if (http == null) {
      synchronized (HttpClientPool.class) {
        if (http == null) {
          http = new HttpClientPool();
        }
      }
    }
    return http;
  }
  /**
   * 构建默认的okHttpClient客户端
   *
   * @return okHttpClient
   */
  public static OkHttpClient.Builder defaultBuilt() {
    TrustManager[] trustManagers = SslSkip.buildTrustManagers();
    return new OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .sslSocketFactory(
            SslSkip.createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
        .retryOnConnectionFailure(true);
  }

  public static void put(String key) {
    put(key, defaultBuilt().build());
  }

  public static void put(String key, OkHttpClient okHttpClient) {
    getInstance().map.put(key, okHttpClient);
  }

  public static void putIfAbsent(String key, OkHttpClient okHttpClient) {
    getInstance().map.putIfAbsent(key, okHttpClient);
  }

  public static OkHttpClient get() {
    return get(DEFAULT_CLIENT_KEY);
  }

  public static OkHttpClient get(String key) {
    return getInstance().map.get(key);
  }
}
