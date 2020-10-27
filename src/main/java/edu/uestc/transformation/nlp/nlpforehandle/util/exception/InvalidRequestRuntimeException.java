package edu.uestc.transformation.nlp.nlpforehandle.util.exception;

public class InvalidRequestRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	private String reason;
	private Exception e;
	
	public InvalidRequestRuntimeException(String message,Exception exception)
	{
		this.message=message;
		this.e=exception;
	}
	public InvalidRequestRuntimeException(String message)
	{
		this.message=message;
	}
	public InvalidRequestRuntimeException(String message,String reason)
	{
		this.message=message;
		this.reason=reason;
	}

}
