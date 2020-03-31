package com.revature;

import org.apache.log4j.Logger;
import com.revature.controller.AccountUi;

public class Main {
  public static Logger log = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {

    AccountUi ui = new AccountUi();
    Main.log.trace("Bank Application started.");
    ui.startBank();
  }
}
