package cn.edu.buaa.sei.jdat.model;

//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Jar Dependency Analysis Toolkit
//  @ File Name : Class.java
//  @ Date : 2012/1/13
//  @ Author : 
//
//

public class Class extends Type {
	
	private Type superclass;
	
	private String superclassName;
	
	public String getSuperclassName() {
		return superclassName;
	}
	
	public void setSuperclassName(String name) {
		this.superclassName = name;
	}
	
	public void setSuperclass(Type type) {
		this.superclass = type;
	}
	
	public Type getSuperclass() {
		return superclass;
	}
	
	public Type[] getInterfaces() {
		if (this.supertypes.contains(superclass)) {
			int pt = 0;
			Type[] ts = new Type[supertypes.size() - 1];
			for(Type t : supertypes) {
				if (t != superclass)
					ts[pt++] = t;
			}
			return ts;
		} else
			return supertypes.toArray(new Type[0]);
	}
	
	public Type[] getSubclass() {
		return this.subtypes.toArray(new Type[0]);
	}

	@Override
	public String toString() {
		return "C" + this.getSignature();
	}

}
