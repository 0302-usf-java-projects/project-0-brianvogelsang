package com.revature.model;

import java.util.Objects;

public class Account implements Comparable<Account> {

  private int id;
  private String username;
  private String password;
  private String name;
  private double funds;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getFunds() {
    return funds;
  }

  public void setFunds(double funds) {
    this.funds = funds;
  }

  public Account() {
    super();

  }

  public Account(int id, String username, String password, String name, double funds) {
    super();
    this.id = id;
    this.username = username;
    this.password = password;
    this.name = name;
    this.funds = funds;
  }
  
  public Account(String username, String password, String name) {
    super();
    this.username = username;
    this.password = password;
    this.name = name;
    this.id = 0;
    this.funds = 0;
  }

  @Override
  public String toString() {
    return "Account [id=" + id + ", username=" + username + ", password=" + password + ", name="
        + name + ", funds=" + funds + "]\n";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Account other = (Account) obj;
    return id == other.id && Objects.equals(username, other.username)
        && Objects.equals(name, other.name) && Objects.equals(password, other.password)
        && funds == other.funds;
  }

  @Override
  public int compareTo(Account o) {
    // TODO Auto-generated method stub
    return this.getId() - o.getId();
  }

}