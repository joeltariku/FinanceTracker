package com.joeltariku.financetracker.exception;

public class EmailAlreadyExistsException extends RuntimeException {
  public EmailAlreadyExistsException(String email) {
    super(String.format("The email '%s' is already registered.", email));
  }
}
