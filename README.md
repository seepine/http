# http

easy http for java

```xml

<dependency>
  <groupId>com.seepine</groupId>
  <artifactId>http</artifactId>
  <version>0.1.0</version>
</dependency>
```

## 一、使用方法

### 1.Get

```java
class GetTest {
  public static void main(String[] args) {
    Response response = Request.get("/user/get").execute();
    System.out.println(response.isOk());
    System.out.println(response.bodyStr());

    // 添加请求头
    Response response = Request.get("/user/get").addHeader("token", "xxx").execute();
  }
}
```

### 2.Post

#### form

```java
class PostTest {
  public static void main(String[] args) {
    Response response = Request.post("/user/form")
      .addForm("username", "admin")
      .addForm("password", "123456")
      .execute();
  }
}
```

#### json

```java
class PostTest {
  public static void main(String[] args) {
    Response response =
      Request.post("/user/add").body(Json.toJson(SysUser.build())).execute();
  }
}
```

### 3.Upload

```java
class UploadTest {
  public static void main(String[] args) {
    AtomicLong progress = new AtomicLong();
    Response response =
      Request.post("/upload")
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

    // 不带进度
    Response response =
      Request.post("/upload")
        .body(new File("C:\\Users\\seepine\\OneDrive\\文档\\downloadVideo.MP4"))
        .execute();
  }
}
```

### 4.Download

```java
class Download {
  public static void main(String[] args) {
    // 不带进度
    Response response = Request.get("/download").execute();
    FileOutputStream outputStream =
      new FileOutputStream("C:\\Users\\seepine\\OneDrive\\文档\\downloadVideo33.MP4");
    try {
      outputStream.write(response.bodyBytes());
      outputStream.flush();
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 带进度
    AtomicLong progress = new AtomicLong();
    FileOutputStream outputStream =
      new FileOutputStream("C:\\Users\\seepine\\OneDrive\\文档\\downloadVideo22.MP4");
    Response response =
      Request.get("/download")
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
    try {
      outputStream.flush();
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
```

### 5.自定义用法

```java
class CustomTest {
  public static void main(String[] args) {
    Request.get("/custom")
      // 自定义body，okHttp的requestBody
      .body(RequestBody)
      // 自定义回调，okHttp的Callback
      .execute(Callback);

  }
}
```

## 二、修改OkHttpClient

### 1.修改默认okHttpClient配置

```java
import com.seepine.http.HttpClientPool;

class Test {
  public static void main(String[] args) {
    HttpClientPool.put(HttpClientPool.DEFAULT_CLIENT_KEY, new OkHttpClient.Builder().followRedirects(false).build());
  }
}
```

### 2.多okHttpClient实例

例如在项目中，请求第三方A接口需要10秒超时，而请求第三方B接口需要30秒超时，此时可配置第二套okHttpClient

```java
import com.seepine.http.Request;

class Test {
  public static void main(String[] args) {
    HttpClientPool.put("server_b", new OkHttpClient.Builder()
      .connectTimeout(30, TimeUnit.SECONDS)
      .build());
    // 此时会使用server_b的okHttpClient
    Request.get("/xxx").httpClientName("server_b").execute();
  }
}
```
