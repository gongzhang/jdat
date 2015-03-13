package cn.edu.buaa.sei.jdat.model;

import cn.edu.buaa.sei.jdat.Utility;

public class TypeDelegator {
	private String signature;
	private Type type = null;
	
	private int count = 0;
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public String getTypeSignature() {
		return signature;
	}
	
	public String getTypeName() {
		return Utility.getTypeName(signature);
	}
	
	public void setTypeSignature(String signature) {
		this.signature = signature;
	}
	
	public TypeDelegator(String name) {
		this.signature = name;
	}
	
	public TypeDelegator(String signature, Type type) {
		this.signature = signature;
		this.type = type;
	}
	
	public void alloc() {
		count++;
	}
	
	public void dealloc() {
		count--;
	}
	
	public boolean canRelease() {
		return count == 0;
	}
}
