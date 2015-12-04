package com.nfortics.mfinanceV2.Application;

public class ApplicationInitialisationException extends Exception {

  private static final long serialVersionUID = 3984397108770463719L;

  public ApplicationInitialisationException() {
    super();
  }

  public ApplicationInitialisationException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public ApplicationInitialisationException(String detailMessage) {
    super(detailMessage);
  }

  public ApplicationInitialisationException(Throwable throwable) {
    super(throwable);
  }

}
