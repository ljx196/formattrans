package edu.uestc.transformation.nlp.nlpforehandle.util.exception;

public class SentenceAnaRuntimeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private String reason;
	private Exception e;
	
	public SentenceAnaRuntimeException(String message,Exception exception)
	{
		this.message=message;
		this.e=exception;
	}
	public SentenceAnaRuntimeException(String message)
	{
		this.message=message;
	}
	public SentenceAnaRuntimeException(String message,String reason)
	{
		this.message=message;
		this.reason=reason;
	}

}
