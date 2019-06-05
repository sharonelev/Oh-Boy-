package com.appsbysha.ohboy.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saying_table")
public class Saying {

  @PrimaryKey(autoGenerate = true)
  private int id;
  private int childId;
  private String title;
  private String description;
  private String date;
  private String age;
  private byte[] photo;


  public Saying(int childId, String title, String description, String date,  byte[] photo){
    this.title = title;
    this.description = description;
    this.date = date;
    this.childId = childId;
    this.photo = photo;
  }

  public int getChildId() {
    return childId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setChildId(int childId) {
    this.childId = childId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public byte[] getPhoto() {
    return photo;
  }

  public void setPhoto(byte[] photo) {
    this.photo = photo;
  }

}

