package com.revature.controller;

import java.util.Scanner;
import com.revature.model.Account;

public class BankController {
  
  public static final Scanner scanner = new Scanner(System.in);
  public static final Scanner wait = new Scanner(System.in);
  
  private static boolean loggedIn = false;
  
  public static Account userAccount;
  
  public static boolean getLogStatus() {
    return loggedIn;
  }
  
  public static void setLogin() {
    loggedIn = true;
  }

  public static void setLogout() {
    loggedIn = false;
  }
}
