package com.revature.unittest;


import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.revature.controller.AccountUi;
import com.revature.exception.PasswordRejectedException;
import com.revature.exception.UsernameRejectedException;
import com.revature.service.AccountService;


public class UnitTest {

  private static AccountUi uiTest;
  private static AccountService asTest;

  @Before
  public void setUp() {
    uiTest = new AccountUi();
    asTest = new AccountService();

  }

  @After
  public void tearDown() {
    uiTest = null;
    asTest = null;
  }

  @Test(expected = PasswordRejectedException.class)
  public void testPasswordIsTooShort() throws PasswordRejectedException {
    asTest.validatePassword("short");
  }

  @Test(expected = PasswordRejectedException.class)
  public void testPasswordIsTooLong() throws PasswordRejectedException {
    String longpass = "Thispasswordismuchtoolong";
    StringBuilder sb = new StringBuilder();

    while (sb.length() < 64) {
      sb.append(longpass);
    }
    asTest.validatePassword(sb.toString());
  }

  @Test(expected = UsernameRejectedException.class)
  public void testUsernameIsTooLong() throws UsernameRejectedException {
    String longpass = "Thispasswordismuchtoolong";
    StringBuilder sb = new StringBuilder();

    while (sb.length() < 32) {
      sb.append(longpass);
    }
    asTest.validateUsername(sb.toString());
  }

  @Test(expected = UsernameRejectedException.class)
  public void testUsernameIsTooShort() throws UsernameRejectedException {
    asTest.validateUsername("abc");
  }
  
  @Test(expected = UsernameRejectedException.class)
  public void testUsernameIsRejectedHasSymbols() throws UsernameRejectedException {
    asTest.validateUsername("%%%%%");
  }
  
  @Test(expected = UsernameRejectedException.class)
  public void testUsernameIsRejectedNull() throws UsernameRejectedException {
    asTest.validateUsername("");
  }
  
  @Test(expected = UsernameRejectedException.class)
  public void testUsernameIsRejectedWhiteSpace() throws UsernameRejectedException {
    asTest.validateUsername("                test");
  }
}
