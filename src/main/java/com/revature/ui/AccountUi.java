package com.revature.ui;

import static com.revature.controller.BankController.*;
import com.revature.controller.BankController;
import com.revature.exception.PasswordRejectedException;
import com.revature.exception.UsernameRejectedException;
import com.revature.exceptions.InvalidCurrencyInputException;
import com.revature.model.Account;
import com.revature.service.AccountService;

public class AccountUi {

  private AccountService as = new AccountService();

  public void debug() {

    // Robert','Robert') drop table accounts";)--
  }

  public void startBank() {

    while (true) {
      while (!getLogStatus()) {
        logMenu();
      }
      while (getLogStatus()) {
        menu();
      }
    }
  }

  public void logMenu() {
    clearScreen();
    System.out.println("Welcome to BV Credit Union:");
    System.out.println("----------------------------");
    System.out.println("#1  REGISTER NEW ACCOUNT");
    System.out.println("#2  LOGIN EXISTING ACCOUNT");
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
      case "0":
      case "#0":
      case "EXIT":
        System.out.println("PROGRAM TERMINATED");
        System.exit(0);
      default:
        notRecognized();
    }
  }

  public void menu() {
    clearScreen();
    System.out.println("Welcome, " + userAccount.getName() + ".");
    System.out.println("-----------------------------------");
    System.out.println("Your balance is $" + as.displayFunds());
    System.out.println("-----------------------------------");
    System.out.println("#1  VIEW BALANCE");
    System.out.println("#2  VIEW TRANSACTIONS");
    System.out.println("#3  WITHDRAW");
    System.out.println("#4  DEPOSIT");
    System.out.println("#5  TRANSFER");
    System.out.println("#6  DELETE ACCOUNT");
    System.out.println("");
    System.out.println("#7 VIEW ACCOUNTS");
    System.out.println("#8 LOOKUP ACCOUNT");

    System.out.println("\n#0  LOGOUT");
    String choice = scanner.nextLine();
    switch (choice) {
      case "1":
      case "#1":
        viewBalance();
        break;
      case "2":
      case "#2":
        System.out.println("not implemented");
        System.out.println("Press any key to return...");
        wait.nextLine();
        break;
      case "3":
      case "#3":
        viewWithdraw();
        break;
      case "4":
      case "#4":
        viewDeposit();
        break;
      case "5":
      case "#5":
        viewTransfer();
        break;
      case "#6":
      case "6":
        uiDeleteAccount();
        break;
      case "#7":
      case "7":
        viewAccounts();
        break;
      case "8":
      case "#8":
        findAccount();
        break;
      case "0":
      case "#0":
        setLogout();
        userAccount = null;
        break;
      default:
        notRecognized();
    }

  }

  public void viewAccounts() {
    System.out.println(as.getAllAccounts());
    System.out.println("Press any key to continue...");
    wait.nextLine();
  }

  public void createAccount() {
    String username;
    String password;
    String name;
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

    System.out.println("Please enter your first name: ");
    name = scanner.nextLine();


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

  public void findAccount() {
    System.out.println("how would you like to find an account?");
    System.out.println("----1 by id");
    System.out.println("----2 by username");
    int chosen = scanner.nextInt();
    switch (chosen) {
      case 1:
        System.out.println(findById());
        menu();
        break;
      case 2:
        System.out.println(findByUsername());
        menu();
        break;
      default:
        System.out.println("please choose an accurate option");
        findAccount();
        break;
    }
  }

  public Account findById() {
    System.out.println("what is the id of the account?");
    int chosen = scanner.nextInt();
    Account a = as.getById(chosen);
    return a;
  }

  public Account findByUsername() {
    System.out.println("What is the username?");
    String chosen = scanner.next();
    Account a = as.getByUsername(chosen);
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
    System.out.println("\n\nPress any key to continue...");
    wait.nextLine();
  }

  public void uiDeleteAccount() {
    clearScreen();
    System.out.println("Would you like to delete account: '" + userAccount.getUsername() + "'?");
    System.out.println("(Y/N)?");
    String input = scanner.nextLine();
    switch (input) {
      case "Y":
      case "y":
        System.out.println("To delete account, enter password:");
        String password = scanner.nextLine();
        try {
          if (as.validatePassword(password)) {
            if (as.authenticate(userAccount.getUsername(), password)) {
              System.out.println("Deleting account...");
              if (as.delete(userAccount.getUsername())) {
                setLogout();
                userAccount = null;
              }
            }
          }
        } catch (PasswordRejectedException e) {
          System.out.println("Invalid password...");
          System.out.println("Logging out for your protection.");
          setLogout();
          userAccount = null;
          System.out.println("Press any key to return...");
          wait.nextLine();
          return;
        }
        break;
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
    System.out.println("Your current balance is $" + as.displayFunds());
    System.out.println("-----------------------------------");
    System.out.println("Press any key to return...");
    wait.nextLine();
  }

  public void viewWithdraw() {
    clearScreen();
    System.out.println("-----------------------------------");
    System.out.println("Your current balance is $" + as.displayFunds());
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
    System.out.println("Your current balance is $" + as.displayFunds());
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
    System.out.println("Press any key to return...");
    wait.nextLine();
    return;
  }

  public void viewTransfer() {
    clearScreen();
    System.out.println("-----------------------------------");
    System.out.println("Your current balance is $" + as.displayFunds());
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
    System.out.println(
        "Confirm transfer of $" + as.formatFunds(amount) + " to " + target.getName() + "?");
    System.out.println("(Y/N)?");
    String answer = scanner.nextLine();
    switch (answer) {
      case "Y":
      case "y":
        as.transferFunds(as.getByUsername(userAccount.getUsername()), target, amount);
        System.out.println("Transfer complete.");
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
