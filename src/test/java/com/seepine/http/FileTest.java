package com.seepine.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class FileTest {
  static String baseUrl = "http://localhost:28484/file";

  public static void main(String[] args) throws FileNotFoundException {
    upload();
    download();
    downloadWithProgress();
  }

  public static void upload() {
    System.out.println();
    System.out.println("start    ====测试上传文件====");
    AtomicLong progress = new AtomicLong();
    Response response =
        Request.post(baseUrl)
            .body(
                new File("C:\\Users\\seepine\\OneDrive\\文档\\downloadVideo.MP4"),
                (currentBytes, contentLength) -> {
                  progress.set(currentBytes * 10000 / contentLength);
                  System.out.println(
                      String.format("Progress: %.2f%%", progress.get() * 1.0 / 100)
                          + " "
                          + currentBytes
                          + "/"
                          + contentLength);
                })
            .execute();
    System.out.println("isOk:" + response.isOk());
    System.out.println("body:" + response.bodyStr());
    System.out.println("end      ====测试上传文件====");
    System.out.println();
  }

  public static void download() throws FileNotFoundException {
    System.out.println();
    System.out.println("start    ====测试下载文件====");
    Response response = Request.get(baseUrl + "/0").execute();
    System.out.println("isOk:" + response.isOk());
    System.out.println("headers:" + response.headers());

    FileOutputStream outputStream =
        new FileOutputStream("C:\\Users\\seepine\\OneDrive\\文档\\downloadVideo33.MP4");
    try {
      outputStream.write(response.bodyBytes());
      outputStream.flush();
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("end      ====测试下载文件====");
    System.out.println();
  }

  public static void downloadWithProgress() throws FileNotFoundException {
    System.out.println();
    System.out.println("start    ====测试下载文件带进度====");
    AtomicLong progress = new AtomicLong();

    FileOutputStream outputStream =
        new FileOutputStream("C:\\Users\\seepine\\OneDrive\\文档\\downloadVideo22.MP4");
    Response response =
        Request.get(baseUrl + "/0")
            .execute(
                (bytes, len, currentBytes, contentLength) -> {
                  try {
                    outputStream.write(bytes, 0, len);
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  progress.set(currentBytes * 10000 / contentLength);
                  System.out.println(
                      contentLength > -1
                          ? String.format("Progress: %.2f%%", progress.get() * 1.0 / 100)
                          : "?%" + " " + currentBytes + "/" + contentLength);
                });
    System.out.println("isOk:" + response.isOk());
    try {
      outputStream.flush();
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("end      ====测试下载文件带进度====");
    System.out.println();
  }
}
