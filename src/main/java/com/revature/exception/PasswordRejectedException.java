package com.revature.exception;

public class PasswordRejectedException extends Exception{

  public PasswordRejectedException(String message) {
    super(message);
  }

  private static final long serialVersionUID = 1L;

}
