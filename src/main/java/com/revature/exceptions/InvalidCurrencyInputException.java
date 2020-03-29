package com.revature.exceptions;

public class InvalidCurrencyInputException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidCurrencyInputException(String message) {
    super(message);
  }
}
