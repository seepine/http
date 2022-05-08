package com.seepine.http.entity;
/**
 * @author seepine
 */
public interface ProgressListener {
  /**
   * 上传进度监听
   *
   * @param currentBytes 已传输
   * @param contentLength 总大小
   */
  void onProgress(long currentBytes, long contentLength);
}
