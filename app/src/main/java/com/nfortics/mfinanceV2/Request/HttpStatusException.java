package com.nfortics.mfinanceV2.Request;

public class HttpStatusException extends Exception {

	private static final long serialVersionUID = -3247385913789898417L;

	private final int httpStatus;
	private final String statusMessage;

	public HttpStatusException(String statusMessage, int httpStatus) {
		this(statusMessage, httpStatus, null);
	}

	public HttpStatusException(String statusMessage, int httpStatus,
							   Throwable throwable) {
		super(statusMessage, throwable);
		this.statusMessage = statusMessage;
		this.httpStatus = httpStatus;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public String getStatusMessage() {
		if (statusMessage.toLowerCase().contains(
				"credentials:name doesn't match any user")) {
			return "Invalid mobile";
		}
		if (statusMessage.toLowerCase().contains(
				"credentials:incorrect password")) {
			return "Invalid password";
		}
		return statusMessage;
	}

}
