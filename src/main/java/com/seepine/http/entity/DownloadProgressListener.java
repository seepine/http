package com.seepine.http.entity;
/**
 * @author seepine
 */
public interface DownloadProgressListener {
  /**
   * 下载进度监听
   *
   * @param bytes 字节
   * @param length 当前bytes长度
   * @param currentBytes 已传输
   * @param contentLength 总大小，无则-1
   */
  void onProgress(byte[] bytes, int length, long currentBytes, long contentLength);
}
