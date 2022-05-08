package com.seepine.http;

public class GetTest {
  static String baseUrl = "http://localhost:28484/get";

  public static void main(String[] args) {
    req();
    path();
    form();
  }

  public static void req() {
    System.out.println();
    System.out.println("start    ====测试无传参====");
    Response response = Request.get(baseUrl).execute();
    System.out.println("isOk:{}" + response.isOk());
    System.out.println("body:{}" + response.bodyStr());
    System.out.println("body:{}" + response.bodyStr());
    System.out.println("end      ====测试无传参====");
    System.out.println();
  }

  public static void path() {
    System.out.println();
    System.out.println("start    ====测试path传参====");
    Response response = Request.get(baseUrl + "/86516").execute();
    System.out.println("isOk:{}" + response.isOk());
    System.out.println("body:{}" + response.bodyStr());
    System.out.println("end      ====测试path传参====");
    System.out.println();
  }

  public static void form() {
    System.out.println();
    System.out.println("start    ====测试form传参====");
    Response response = Request.get(baseUrl + "/form").addForm("id", 15666L).execute();
    System.out.println("isOk:{}" + response.isOk());
    System.out.println("body:{}" + response.bodyStr());
    System.out.println("end      ====测试form传参====");
    System.out.println();
  }
}
