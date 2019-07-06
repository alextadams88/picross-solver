package com.atadams.picross.exception;

public class PicrossSolverException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PicrossSolverException() {
		super();
	}
	
	public PicrossSolverException(String message) {
		super(message);
	}
	
	public PicrossSolverException(String message, Exception ex) {
		super(message, ex);
	}

}
