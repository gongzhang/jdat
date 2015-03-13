package cn.edu.buaa.sei.jdat.exception;

public class InvalidURIException extends Exception {

	private static final long serialVersionUID = 1112995193391976255L;

	private String invalidURL;
	
	public InvalidURIException(String url, String msg) {
		super(msg);
		this.invalidURL = url;
	}
	
	public String getInvalidURL() {
		return invalidURL;
	}
}
