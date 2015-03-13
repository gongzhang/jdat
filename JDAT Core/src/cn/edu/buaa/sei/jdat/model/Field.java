package cn.edu.buaa.sei.jdat.model;
//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Jar Dependency Analysis Toolkit
//  @ File Name : Field.java
//  @ Date : 2012/1/13
//  @ Author : 
//
//

public class Field extends Member {
	private TypeDelegator fieldType;
	
	private boolean fieldTypeIsArray;

	public Type getType() {
		return this.fieldType.getType();
	}
	
	public void setArray(boolean isArray) {
		this.fieldTypeIsArray = isArray;
	}
	
	public boolean isArray() {
		return this.fieldTypeIsArray;
	}
	
	public void setTypeDelegator(TypeDelegator delegator) {
		this.fieldType = delegator;
	}
	
	public String getTypeSignature() {
		return this.fieldType.getTypeSignature();
	}
	
	public String getTypeName() {
		return this.fieldType.getTypeName();
	}
	
	@Override
	public String getSignature() {
		return this.getOwner().getSignature() + "." + this.getName() + ":" + this.fieldType.getTypeSignature();
	}
}