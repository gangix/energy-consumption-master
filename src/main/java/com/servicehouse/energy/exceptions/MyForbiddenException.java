package com.servicehouse.energy.exceptions;

public class MyForbiddenException extends RuntimeException {

	public MyForbiddenException() {
        super();
    }
    public MyForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
    public MyForbiddenException(String message) {
        super(message);
    }
    public MyForbiddenException(Throwable cause) {
        super(cause);
    }
    
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getMessage() {
		return "Process is forbidden!";
	}
}