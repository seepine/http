package com.seepine.http.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SysUser {
  Long id;
  String name;
  String idCard;
  Integer age;
  Double height;
  LocalDate birthday;
  List<String> tags;
  LocalDateTime createTime;
  LocalDateTime updateTime;

  public static SysUser build() {
    SysUser sysUser = new SysUser();
    sysUser.setId(123456L);
    sysUser.setName("张三");
    sysUser.setIdCard("350212199210235024");
    sysUser.setAge(35);
    sysUser.setHeight(165.8);
    sysUser.setBirthday(LocalDate.now());
    sysUser.setTags(Arrays.asList("乐观", "向上"));
    sysUser.setCreateTime(LocalDateTime.now());
    return sysUser;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Double getHeight() {
    return height;
  }

  public void setHeight(Double height) {
    this.height = height;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public LocalDateTime getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(LocalDateTime updateTime) {
    this.updateTime = updateTime;
  }
}
