package com.appsbysha.ohboy.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "children_table")
public class Child {

  @PrimaryKey(autoGenerate = true)
  private int id;
  private String name;
  private String dob;
  private byte[] profilePic;

   public Child(String name, String dob, byte[] profilePic){
    this.name = name;
    this.dob = dob;
    this.profilePic = profilePic;
  }

/*
  public Child(int id, String name, String dob){ //redundant. only used for pre-populating db
    this.id = id;
    this.name = name;
    this.dob = dob;
  }
*/


  public byte[] getProfilePic() {
    return profilePic;
  }

  public void setProfilePic(byte[] profilePic) {
    this.profilePic = profilePic;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDob() {
    return dob;
  }

  public void setDob(String dob) {
    this.dob = dob;
  }
}
