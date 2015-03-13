package cn.edu.buaa.sei.jdat.search.exception;

public class InvalidQueryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7005562840255505842L;
	
	public static final int WARNING = 1;
	public static final int ERROR = 2;
	
	private String query;
	private int kind;
	
	public InvalidQueryException(String query, int kind) {
		this.query = query;
		this.kind = kind;
	}
	
	public int getKind() {
		return kind;
	}
	
	public String getQuery() {
		return query;
	}

}
