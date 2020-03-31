package com.revature.controller;

import static com.revature.controller.BankController.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import com.revature.Main;
import com.revature.exception.InvalidCurrencyInputException;
import com.revature.exception.PasswordRejectedException;
import com.revature.exception.UsernameRejectedException;
import com.revature.model.Account;
import com.revature.service.AccountService;

public class AccountUi {
  private AccountService as = new AccountService();

  public void debug() {
    Main.log.debug("Debug menu entered.");
  }

  public void startBank() {
    while (true) {
      if (as.getAdminLoggedIn()) {
        viewManagerMenu();
      } else if (getLogStatus()) {
        menu();
      } else if (!getLogStatus()) {
        logMenu();
      }
    }
  }

  public void logMenu() {
    Main.log.info("Login Menu Entered.");
    clearScreen();
    System.out.println("Welcome to BV Credit Union:");
    System.out.println("----------------------------");
    System.out.println("#1  REGISTER NEW ACCOUNT");
    System.out.println("#2  LOGIN EXISTING ACCOUNT");
    System.out.println("----------------------------\n");
    System.out.println("#3  LOGIN AS BANK MANAGER");
    System.out.println("----------------------------\n");
    System.out.println("#4 HELP MENU");
    System.out.println("\n#0  EXIT");
    String choice = scanner.nextLine();
    switch (choice) {
      case "1":
      case "#1":
        createAccount();
        break;
      case "2":
      case "#2":
        logIntoAccount();
        break;
      case "3":
      case "#3":
        viewManagerLogin();
        break;
      case "4":
      case "#4":
        viewHelpMenu();
        break;
      case "0":
      case "#0":
      case "EXIT":
        System.out.println("PROGRAM TERMINATED");
        Main.log.info("Bank Application exited.");
        System.exit(0);
      case "debug":
        debug();
      default:
        notRecognized();
    }
  }

  private void viewHelpMenu() {
    Main.log.info("Help Menu logged in.");
    clearScreen();
    System.out.println("Welcome to BV Credit Union:");
    System.out.println("---------------------------");
    System.out.println("At this bank,\nEach account is tied to a unique ID and USERNAME");
    System.out.println("Any user can create multiple accounts, but will need to track their");
    System.out.println("account usernames and passwords.\n");
    System.out.println("Usernames must be between 3 and 32 characters. No symbols or spaces.");
    System.out.println("Passwords must be between 8 and 65 characters.\n");
    System.out.println("Customers can search for public accounts, but cannot see balance or passwords.");
    System.out.println("Employee level users can see all user information.\n");
    System.out.println("Press any key to return...");
    wait.nextLine();
  }

  private void viewManagerLogin() {
    clearScreen();
    System.out.println("Please provide admin password:");
    String password = scanner.nextLine();
    switch (password) {
      case "admin":
        as.setAdminLoggedIn();
        Main.log.info("Bank Manager logged in.");
        break;
      default:
        notRecognized();
    }
  }

  private void viewManagerMenu() {
    clearScreen();
    System.out.println("Welcome, ADMIN.");
    System.out.println("--------------------");
    System.out.println("#1 VIEW ACCOUNTS");
    System.out.println("#2 LOOKUP ACCOUNT");
    System.out.println("---------------------------------------");
    System.out.println("#3 SET TARGET ACCOUNT AS EMPLOYEE");
    System.out.println("#4 REMOVE TARGET ACCOUNT AS EMPLOYEE");
    System.out.println("---------------------------------------");
    System.out.println("#5 VIEW HISTORY OF TARGET ACCOUNT");
    System.out.println("#6 DELETE TARGET ACCOUNT");
    System.out.println("---------------------------------------");
    System.out.println("#7 VIEW LOGS");
    System.out.println("#8 DELETE LOGS");
    System.out.println("\n#0  LOGOUT FROM ADMIN");
    String choice = scanner.nextLine();
    switch (choice) {
      case "#1":
      case "1":
        viewAccounts();
        System.out.println("Press any key to continue...");
        wait.nextLine();
        break;
      case "2":
      case "#2":
        Account a = findAccount();
        if (a != null) {
          System.out.println(a);
          System.out.println("Press any key to continue...");
          wait.nextLine();
        }
        break;
      case "3":
      case "#3":
        viewPromoteEmployee();
        break;
      case "4":
      case "#4":
        viewDemoteEmployee();
        break;
      case "5":
      case "#5":
        viewMyLog();
        userAccount = null;
        break;
      case "6":
      case "#6":
        uiDeleteAccount();
        userAccount = null;
        break;
      case "7":
      case "#7":
        viewManagerLogs();
        break;
      case "8":
      case "#8":
        deleteManagerLogs();
        break;
      case "0":
      case "#0":
      case "EXIT":
        as.setAdminLoggedOut();
        Main.log.info("Bank Manager logged out.");
        break;
      default:
        notRecognized();
    }
  }

  private void viewManagerLogs() {
    Scanner logScanner;
    clearScreen();
    System.out.println("Manager Logs");
    System.out.println("------------------------");
    Main.log.info("Viewed Manager Logs.");
    try {
      logScanner = new Scanner(new File("logs/Logs.log"));
      while (logScanner.hasNextLine()) {
        System.out.println(logScanner.nextLine());
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    System.out.println("\n\nPress any key to return.");
    wait.nextLine();
  }

  private void deleteManagerLogs() {
    clearScreen();
    System.out.println("Delete all logs?");
    System.out.println("(Y/N)?");
    String choice = scanner.nextLine();
    switch (choice) {
      case "Y":
      case "y":
        PrintWriter deleteLogs;
        try {
          deleteLogs = new PrintWriter("logs/Logs.log");
          deleteLogs.print("");
          deleteLogs.close();
          System.out.println("Logs cleared.");
          System.out.println("Press any key to return.");
          wait.nextLine();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        break;
      default:
        System.out.println("Logs not deleted.");
        break;
    }
  }

  private void viewDemoteEmployee() {
    viewAccounts();
    Account a = findAccount();
    if (a == null) {
      return;
    }
    if (!a.isEmployee()) {
      System.out.println("ERROR! " + a.getName() + " is not an Employee!");
      System.out.println("Press any key to return...");
      wait.nextLine();
      return;
    }
    System.out.println("Would you like to remove " + a.getName() + " from Employee list?");
    System.out.println("(Y/N)?");
    String input = scanner.nextLine();
    switch (input) {
      case "Y":
      case "y":
        as.demoteEmployee(a);
        Main.log.info(a.getUsername() + " is no longer an Employee.");
        System.out.println("Success! " + a.getName() + " is no longer an Employee!");
        System.out.println("Press any key to continue...");
        wait.nextLine();
        break;
      default:
        System.out.println("Press any key to return...");
        wait.nextLine();
    }
  }

  private void viewPromoteEmployee() {
    viewAccounts();
    Account a = findAccount();
    if (a == null) {
      return;
    }
    if (a.isEmployee()) {
      System.out.println("ERROR! " + a.getName() + " is already an Employee!");
      System.out.println("Press any key to return...");
      wait.nextLine();
      return;
    }
    System.out.println("Would you like to set " + a.getName() + " as Employee?");
    System.out.println("(Y/N)?");
    String input = scanner.nextLine();
    switch (input) {
      case "Y":
      case "y":

        as.promoteEmployee(a);
        Main.log.info(a.getUsername() + " is promoted to Employee.");
        System.out.println("Success! " + a.getName() + " is now an Employee!");
        System.out.println("Press any key to continue...");
        wait.nextLine();
        break;
      default:
        System.out.println("Press any key to return...");
        wait.nextLine();
    }
  }

  public void menu() {
    clearScreen();
    System.out.println("Welcome, " + userAccount.getName() + ".");
    System.out.println("-----------------------------------");
    System.out.println("Your balance is " + as.displayFunds());
    System.out.println("-----------------------------------");
    System.out.println("#1  VIEW BALANCE");
    System.out.println("#2  VIEW TRANSACTION HISTORY");
    System.out.println("#3  VIEW FRIEND LIST");
    System.out.println("#4  WITHDRAW");
    System.out.println("#5  DEPOSIT");
    System.out.println("#6  TRANSFER");
    System.out.println("#7  DELETE MY ACCOUNT");
    if (userAccount.isEmployee()) {
      System.out.println("\n--Employee Options--");
      System.out.println("#8 VIEW ACCOUNTS");
      System.out.println("#9 LOOKUP ACCOUNT");
      System.out.println("---------------------");
    }
    System.out.println("\n#0  LOGOUT");
    String choice = scanner.nextLine();
    switch (choice) {
      case "1":
      case "#1":
        viewBalance();
        break;
      case "2":
      case "#2":
        viewMyLog();
        break;
      case "3":
      case "#3":
        clearScreen();
        viewFriendsList();
        System.out.println("Press any key to return...");
        wait.nextLine();
        break;
      case "4":
      case "#4":
        viewWithdraw();
        break;
      case "5":
      case "#5":
        viewDeposit();
        break;
      case "6":
      case "#6":
        viewTransfer();
        break;
      case "#7":
      case "7":
        uiDeleteAccount();
        break;
      case "#8":
      case "8":
        if (userAccount.isEmployee()) {
          viewAccounts();
          System.out.println("Press any key to continue...");
          wait.nextLine();
        } else {
          notRecognized();
        }
        break;
      case "9":
      case "#9":
        if (userAccount.isEmployee()) {
          Account a = findAccount();
          if (a != null) {
            System.out.println(a);
            System.out.println("Press any key to continue...");
            wait.nextLine();
          }
        } else {
          notRecognized();
        }
        break;
      case "0":
      case "#0":
        setLogout();
        Main.log.info(userAccount.getUsername() + " logged out.");
        userAccount = null;
        break;
      default:
        notRecognized();
    }
  }

  private void viewMyLog() {
    Scanner myScanner;
    clearScreen();
    if (userAccount == null) {
      viewAccounts();
      System.out.println("Choose an account to view.");
      userAccount = findAccount();
      if (userAccount == null) {
        return;
      }
      System.out.println(userAccount);
    }
    System.out.println("History of " + userAccount.getUsername());
    System.out.println("------------------------");
    Main.log.info(userAccount.getUsername() + " viewed transaction history.");
    try {
      myScanner = new Scanner(new File("logs/Logs.log"));
      while (myScanner.hasNextLine()) {
        String output = myScanner.nextLine().toString();

        if (output.contains(userAccount.getUsername())) {
          System.out.println(output);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    System.out.println("\n\nPress any key to return.");
    wait.nextLine();
  }

  public void viewAccounts() {
    System.out.println(as.getAllAccounts());
  }

  public void createAccount() {
    String username;
    String password;
    String name = "";
    clearScreen();
    System.out.println("Welcome to Account creation!");
    System.out.println("----------------------------------------");
    System.out.println("Please enter a username: ");
    username = scanner.nextLine().toUpperCase();
    try {
      as.validateUsername(username);
    } catch (UsernameRejectedException e) {
      System.out.println(e.getLocalizedMessage());
      System.out.println("Press any key to return...");
      wait.nextLine();
      return;
    }
    System.out.println("Please enter a password: ");
    password = scanner.nextLine();
    try {
      as.validatePassword(password);
    } catch (PasswordRejectedException e) {
      System.out.println(e.getLocalizedMessage());
      System.out.println("Press any key to return...");
      wait.nextLine();
      return;
    }
    int counter = 0;
    while ((!(name.matches(AccountService.usernamePattern))) || name.length() > 24) {
      if (counter > 0) {
        System.out.println("Please only use letters or numbers for your nickname.");
        if (name.length() > 24) {
          System.out.println("Please keep length of nickname under 24 characters.");
        }
      }
      System.out.println("Please enter your first name or alias: ");
      name = scanner.nextLine();
      counter++;
    }
    Account a = as.insert(username, password, name);
    BankController.userAccount = as.getByUsername(a.getUsername());
    if (as.authenticate(username, password)) {
      loginSuccess();
    }
    if (!getLogStatus()) {
      userAccount = null;
      System.out.println("Failed to log in with username and password.");
      notRecognized();
    }
  }

  public Account findAccount() {
    System.out.println("CHOOSE OPTION: ");
    System.out.println("#1 SEARCH BY ID");
    System.out.println("#2 SEARCH BY USERNAME");
    String choice = scanner.nextLine();
    switch (choice) {
      case "1":
      case "#1":
        return findById();
      case "2":
      case "#2":
        return findByUsername();
      default:
        notRecognized();
        return null;
    }
  }

  public Account findById() {
    System.out.println("ENTER TARGET ID #:");
    String idInput = scanner.nextLine();
    try {
      Integer.parseInt(idInput);
    } catch (java.lang.NumberFormatException e) {
      System.out.println("Please enter a valid number.");
      System.out.println("Press any key to return...");
      wait.nextLine();
      return null;
    }
    int idInt = Integer.parseInt(idInput);
    Account a = as.getById(idInt);
    if (a == null) {
      System.out.println("Press any key to return...");
      wait.nextLine();
      return null;
    } else
      return a;
  }

  public Account findByUsername() {
    System.out.println("ENTER TARGET USERNAME:");
    String usernameInput = scanner.nextLine().toUpperCase();
    Account a = as.getByUsername(usernameInput);
    if (a == null) {
      System.out.println("Username does not exist.");
      System.out.println("Press any key to return...");
      wait.nextLine();
      return null;
    } else
      return a;
  }

  public void logIntoAccount() {
    System.out.println("Please log in. Provide registered username:");
    String usernameInput = scanner.nextLine().toUpperCase();
    BankController.userAccount = as.getByUsername(usernameInput);
    if (userAccount == null) {
      notRecognized();
    } else {
      System.out.println("Provide password:");
      String passwordInput = scanner.nextLine();
      if (as.authenticate(usernameInput, passwordInput)) {
        loginSuccess();
        Main.log.info(userAccount.getUsername() + " logged in.");
      }
      if (!getLogStatus()) {
        userAccount = null;
        System.out.println("Failed to log in with username and password.");
        notRecognized();
      }
    }
  }

  public static void clearScreen() {
    System.out.println(new String(new char[99]).replace('\0', '\n'));
  }

  public static void notRecognized() {
    clearScreen();
    System.out.println("Sorry, that wasn't recognized...");
    System.out.println("Press any key to return...");
    String input = scanner.nextLine();
    if (input.equals("EXIT")) {
      System.out.println("PROGRAM TERMINATED");
      System.exit(0);
    }
  }

  public static void loginSuccess() {
    setLogin();
    clearScreen();
    System.out.println("LOGIN SUCCESS!\n");
    System.out.println("Welcome, " + userAccount.getName());
    Main.log.info(userAccount.getUsername() + " created account.");
    System.out.println("\n\nPress any key to continue...");
    wait.nextLine();
  }

  public void uiDeleteAccount() {
    clearScreen();
    if (userAccount == null) {
      viewAccounts();
      System.out.println("Find an account to delete.");
      userAccount = findAccount();
      if (userAccount == null) {
        return;
      }
      System.out.println(userAccount);
    }
    System.out.println("Would you like to delete account: '" + userAccount.getUsername() + "'?");
    System.out.println("(Y/N)?");
    String input = scanner.nextLine();
    switch (input) {
      case "Y":
      case "y":
        System.out.println("To delete account, enter password of that account:");
        String password = scanner.nextLine();
        try {
          if (as.validatePassword(password)) {
            if (as.authenticate(userAccount.getUsername(), password)) {
              System.out.println("Deleting account...");
              if (as.delete(userAccount.getUsername())) {
                setLogout();
                Main.log.info(userAccount.getUsername() + " account was deleted.");
                userAccount = null;
              }
            }
          }
        } catch (PasswordRejectedException e) {
          System.out.println(e.getLocalizedMessage());
        }
        System.out.println("Press any key to return...");
        wait.nextLine();
        return;
      case "N":
      case "n":
        break;
      default:
        notRecognized();
        break;
    }
  }

  public void viewBalance() {
    clearScreen();
    System.out.println("-----------------------------------");
    System.out.println("Your current balance is " + as.displayFunds());
    System.out.println("-----------------------------------");
    System.out.println("Press any key to return...");
    Main.log.info(userAccount.getUsername() + " viewed balance: " + as.displayFunds());
    wait.nextLine();
  }

  public void viewWithdraw() {
    clearScreen();
    System.out.println("-----------------------------------");
    System.out.println("Your current balance is " + as.displayFunds());
    System.out.println("-----------------------------------");
    System.out.println("Welcome to ATM Withdraw App");
    System.out.println("How much money would you like to withdraw?");
    String input = scanner.nextLine();
    try {
      as.validateCurrencyInput(input);
    } catch (InvalidCurrencyInputException e) {
      System.out.println(e.getLocalizedMessage());
      System.out.println("Press any key to return...");
      wait.nextLine();
      return;
    }
    clearScreen();
    System.out.println("Dispensing $" + input + " as cash...");
    as.subtractFunds(as.getByUsername(userAccount.getUsername()), Double.parseDouble(input));
    System.out.println("Done!");
    Main.log.info(userAccount.getUsername() + " withdrew $" + input);
    System.out.println("Press any key to return...");
    wait.nextLine();
    return;
  }

  public void viewDeposit() {
    clearScreen();
    if (as.getByUsername(userAccount.getUsername()).getFunds() > as.getMaxBalance()) {
      System.out.println("Try withdrawing some money before you become the worlds richest person.");
      System.out.println("Press any key to return...");
      wait.nextLine();
      return;
    }
    System.out.println("-----------------------------------");
    System.out.println("Your current balance is " + as.displayFunds());
    System.out.println("-----------------------------------");
    System.out.println("Welcome to ATM Deposit App");
    System.out.println("Please insert cash.");
    System.out.println("How much money would you like to add?");
    String input = scanner.nextLine();
    try {
      as.validateCurrencyDispense(input);
    } catch (InvalidCurrencyInputException e) {
      System.out.println(e.getLocalizedMessage());
      System.out.println("Press any key to return...");
      wait.nextLine();
      return;
    }
    clearScreen();
    System.out.println("Adding $" + input + " to your account....");
    as.addFunds(as.getByUsername(userAccount.getUsername()), Double.parseDouble(input));
    System.out.println("Done!");
    Main.log.info(userAccount.getUsername() + " deposited $" + input);
    System.out.println("Press any key to return...");
    wait.nextLine();
    return;
  }

  public void viewFriendsList() {
    System.out.println("Friends List:");
    for (Account a : as.getAllAccounts()) {
      System.out.println(a.toStringCasual());
    }
  }

  public void viewTransfer() {
    clearScreen();
    viewFriendsList();
    System.out.println("-----------------------------------");
    System.out.println("Your current balance is " + as.displayFunds());
    System.out.println("-----------------------------------");
    System.out.println("Welcome to Funds Transfer App");
    System.out.println("Enter the USERNAME of the account for your transfer.");
    String username = scanner.nextLine().toUpperCase();
    if (username.equals(userAccount.getUsername())) {
      System.out.println("You cannot transfer to yourself.");
      System.out.println("Process aborted.");
      System.out.println("Press any key to return...");
      wait.nextLine();
      return;
    }
    Account target = as.getByUsername(username);
    if (target == null) {
      System.out.println("Username not found.");
      System.out.println("Process aborted.");
      System.out.println("Press any key to return...");
      wait.nextLine();
      return;
    }
    System.out.println("How much would you like to transfer to " + target.getName() + "?");
    String amountString = scanner.nextLine();
    try {
      as.validateCurrencyInput(amountString);
    } catch (InvalidCurrencyInputException e) {
      System.out.println(e.getLocalizedMessage());
      System.out.println("Press any key to return...");
      wait.nextLine();
      return;
    }

    double amount = Double.parseDouble(amountString);
    System.out.println("Confirm transfer of " + AccountService.formatFunds(amount) + " to "
        + target.getName() + "?");
    System.out.println("(Y/N)?");
    String answer = scanner.nextLine();
    switch (answer) {
      case "Y":
      case "y":
        as.transferFunds(as.getByUsername(userAccount.getUsername()), target, amount);
        System.out.println("Transfer complete.");
        Main.log.info(
            userAccount.getUsername() + " transferred $" + amount + " to " + target.getUsername());
        System.out.println("Press any key to return...");
        wait.nextLine();
        break;
      default:
        System.out.println("Process aborted.");
        System.out.println("Press any key to return...");
        wait.nextLine();
        return;
    }
  }
}
