package cn.edu.buaa.sei.jdat.exception;

import cn.edu.buaa.sei.jdat.model.Jar;

public class JarLoadFailedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1630148011776666913L;
	private Jar source;
	
	public JarLoadFailedException(Jar source) {
		this.source = source;
	}
	
	public Jar getSource() {
		return source;
	}
}
