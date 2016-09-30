package com.giift.formr.utils;


import android.support.test.runner.AndroidJUnit4;

import com.giift.formr.field.ButtonChoice;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author  vieony on 9/29/2016.
 */

@RunWith(AndroidJUnit4.class)
public class UtilsTest {

  @Test
  public void IsValidEmail01() {
    String email = "email@domain.com";
    assertTrue("Valid email", Utils.IsEmailValid(email));
  }

  @Test
  public void IsValidEmail02() {
    String email = "firstname.lastname@domain.com";
    assertTrue("Email contains dot in the address field", Utils.IsEmailValid(email));
  }

  @Test
  public void IsValidEmail03() {
    String email = "email@subdomain.domain.com";
    assertTrue("Email contains dot with subdomain", Utils.IsEmailValid(email));
  }

  @Test
  public void IsValidEmail04() {
    String email = "firstname+lastname@domain.com";
    assertTrue("Plus sign is considered valid character", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid08() {
    String email = "1234567890@domain.com";
    assertTrue("Digits in address are valid", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid09() {
    String email = "email@domain-one.com";
    assertTrue("Dash in domain name is valid", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid10() {
    String email = "_______@domain.com";
    assertTrue("Underscore in the address field is valid", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid11() {
    String email = "email@domain.name";
    assertTrue(".name is valid Top Level Domain name", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid12() {
    String email = "email@domain.co.jp";
    assertTrue("Dot in Top Level Domain name also considered valid (use co.jp as example here)", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid13() {
    String email = "firstname-lastname@domain.com";
    assertTrue("Dash in address field is valid", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid14() {
    String email = "plainaddress";
    assertFalse("Missing @ sign and domain", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid15() {
    String email = "#@%^%#$@#$@#.com";
    assertFalse("Garbage", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid16() {
    String email = "@domain.com";
    assertFalse("Missing username", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid17() {
    String email = "Joe Smith <email@domain.com>";
    assertFalse("Encoded html within email is invalid", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid18() {
    String email = "email.domain.com";
    assertFalse("Missing @", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid19() {
    String email = "email@domain@domain.com";
    assertFalse("Two @ sign", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid20() {
    String email = ".email@domain.com";
    assertFalse("Leading dot in address is not allowed", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid21() {
    String email = "email.@domain.com";
    assertFalse("Trailing dot in address is not allowed", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid22() {
    String email = "email..email@domain.com";
    assertFalse("Multiple dots", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid23() {
    String email = "あいうえお@domain.com";
    assertTrue("Unicode char as address", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid24() {
    String email = "email@domain.com (Joe Smith)";
    assertFalse("Text followed email is not allowed", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid25() {
    String email = "email@domain";
    assertFalse("Missing top level domain (.com/.net/.org/etc)", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid26() {
    String email = "email@-domain.com";
    assertFalse("Leading dash in front of domain is invalid", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid27() {
    String email = "email@domain.web";
    assertFalse(".web is not a valid top level domain", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid28() {
    String email = "email@111.222.333.44444";
    assertFalse("Invalid IP format", Utils.IsEmailValid(email));
  }

  @Test
  public void IsEmailValid29() {
    String email = "email@domain..com";
    assertFalse("Multiple dot in the domain portion is invalid", Utils.IsEmailValid(email));
  }

}