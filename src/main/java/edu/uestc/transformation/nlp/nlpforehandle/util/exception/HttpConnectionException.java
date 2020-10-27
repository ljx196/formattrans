package edu.uestc.transformation.nlp.nlpforehandle.util.exception;

public class HttpConnectionException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	private Exception e;
	
	public HttpConnectionException(String message,Exception exception)
	{
		this.message=message;
		this.e=exception;
	}
	public HttpConnectionException(String message)
	{
		this.message=message;
	}

}
