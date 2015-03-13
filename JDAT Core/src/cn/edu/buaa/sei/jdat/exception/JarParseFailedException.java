package cn.edu.buaa.sei.jdat.exception;

import cn.edu.buaa.sei.jdat.model.Jar;

public class JarParseFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3011796471240226188L;

	private Jar source;
	
	public JarParseFailedException(Jar source) {
		this.source = source;
	}
	
	public Jar getSource() {
		return source;
	}
}
