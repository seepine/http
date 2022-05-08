package com.seepine.http;

import com.seepine.http.entity.SysUser;
import com.seepine.json.Json;

public class PostTest {
  static String baseUrl = "http://localhost:28484/post";

  public static void main(String[] args) {
    req();
    path();
    form();
    json();
  }

  public static void req() {
    System.out.println();
    System.out.println("start    ====测试无传参====");
    Response response = Request.post(baseUrl).execute();
    System.out.println("isOk:{}" + response.isOk());
    System.out.println("body:{}" + response.bodyStr());
    System.out.println("end      ====测试无传参====");
    System.out.println();
  }

  public static void path() {
    System.out.println();
    System.out.println("start    ====测试path传参====");
    Response response = Request.post(baseUrl + "/86516").execute();
    System.out.println("isOk:{}" + response.isOk());
    System.out.println("body:{}" + response.bodyStr());
    System.out.println("end      ====测试path传参====");
    System.out.println();
  }

  public static void form() {
    System.out.println();
    System.out.println("start    ====测试form传参====");
    Response response = Request.post(baseUrl + "/form").addForm("id", 15666L).execute();
    System.out.println("isOk:{}" + response.isOk());
    System.out.println("body:{}" + response.bodyStr());
    System.out.println("end      ====测试form传参====");
    System.out.println();
  }

  public static void json() {
    System.out.println();
    System.out.println("start    ====测试json传参====");
    Response response =
        Request.post(baseUrl + "/json").body(Json.toJson(SysUser.build())).execute();
    System.out.println("isOk:{}" + response.isOk());
    System.out.println("body:{}" + response.bodyStr());
    System.out.println("end      ====测试json传参====");
    System.out.println();
  }
}
