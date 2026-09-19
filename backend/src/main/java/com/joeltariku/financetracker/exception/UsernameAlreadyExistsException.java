package com.joeltariku.financetracker.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
  public UsernameAlreadyExistsException(String username) {
    super(String.format("Their is already a user with the username '%s'.", username));
  }
}
