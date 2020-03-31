package com.revature.service;

import static com.revature.controller.BankController.userAccount;
import java.text.DecimalFormat;
import java.util.List;
import com.revature.controller.BankController;
import com.revature.exception.InvalidCurrencyInputException;
import com.revature.exception.PasswordRejectedException;
import com.revature.exception.UsernameRejectedException;
import com.revature.model.Account;
import com.revature.repository.AccountDao;

public class AccountService {

  public AccountDao adao;
  private static boolean adminLoggedIn = false;
  private static final int REQUIRED_PASSWORD_LENGTH = 8;
  private static final double MAX_DEPOSIT = 999999999;
  private static final double MAX_BALANCE = 113999999999D;
  private static DecimalFormat df = new DecimalFormat("#.##");
  public static final String usernamePattern = "\\w+"; // allows letters and numbers, no symbols
  private static final String currencyPattern = "\\d+"; // allows only numbers

  {
    adao = new AccountDao();
  }

  public boolean getAdminLoggedIn() {
    return adminLoggedIn;
  }

  public void setAdminLoggedIn() {
    adminLoggedIn = true;
  }

  public void setAdminLoggedOut() {
    adminLoggedIn = false;
  }

  public double getMaxBalance() {
    return MAX_BALANCE;
  }

  public int getRequiredPasswordLength() {
    return REQUIRED_PASSWORD_LENGTH;
  }

  public List<Account> getAllAccounts() {
    return adao.findAll();
  }

  public Account getById(int id) {
    return adao.findById(id);
  }

  public Account insert(String username, String password, String name) {
    return adao.insert(new Account(username, password, name));
  }

  public Account getByUsername(String s) {
    return adao.findByString(s);
  }

  public boolean authenticate(String username, String password) {
    return BankController.userAccount.getUsername().equals(username)
        && BankController.userAccount.getPassword().equals(password);
  }

  public boolean validateUsername(String s) throws UsernameRejectedException {
    if (!(s.matches(usernamePattern))) {
      throw new UsernameRejectedException("You cannot use whitespace or symbols in your username.");
    } else if (s.length() < 4) {
      throw new UsernameRejectedException("Username cannot be less than 3 characters.");
    } else if (s.length() > 32) {
      throw new UsernameRejectedException("Username cannot be longer than 32 characters.");
    } else if (adao.findByString(s) != null) {
      throw new UsernameRejectedException("That username is already taken.");
    } else {
      return true;
    }
  }

  public boolean validatePassword(String s) throws PasswordRejectedException {
    if (s.length() < 8) {
      throw new PasswordRejectedException("Passwords must be at least 8 characters in length.");
    } else if (s.length() > 64) {
      throw new PasswordRejectedException("Passwords cannot be more than 64 characters.");
    } else {
      return true;
    }
  }

  public boolean validateCurrencyInput(String s) throws InvalidCurrencyInputException {
    if (!(s.matches(currencyPattern))) {
      throw new InvalidCurrencyInputException("Please only enter digits.");
    } else if (getByUsername(userAccount.getUsername()).getFunds() < Double.parseDouble(s)) {
      throw new InvalidCurrencyInputException("Insufficient funds.");
    }
    return true;
  }

  public boolean validateCurrencyDispense(String s) throws InvalidCurrencyInputException {
    if (!(s.matches(currencyPattern))) {
      throw new InvalidCurrencyInputException("Please only enter digits.");
    } else if (Double.parseDouble(s) > MAX_DEPOSIT) {
      throw new InvalidCurrencyInputException(
          "That amount is too high...\nOnly add numbers less than one billion please.");
    }
    return true;
  }

  public boolean delete(String s) {
    return adao.delete(adao.findByString(s));
  }

  public boolean subtractFunds(Account t, Double d) {
    double result = t.getFunds() - d;
    return adao.updateFunds(t, result);
  }

  public boolean addFunds(Account t, Double d) {
    double result = t.getFunds() + d;
    return adao.updateFunds(t, result);
  }

  public String displayFunds() {
    return "$" + df.format(getByUsername(userAccount.getUsername()).getFunds());
  }

  public static String formatFunds(Double d) {
    return "$" + df.format(d);
  }

  public boolean transferFunds(Account user, Account target, double amount) {
    subtractFunds(user, amount);
    addFunds(target, amount);
    return true;
  }

  public void promoteEmployee(Account a) {
    adao.updateEmployeeStatus(a, true);
  }

  public void demoteEmployee(Account a) {
    adao.updateEmployeeStatus(a, false);
  }
}
