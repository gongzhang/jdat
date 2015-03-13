package cn.edu.buaa.sei.jdat.graph;

import cn.edu.buaa.sei.jdat.model.Type;

public class TypeDependency implements IDependency<Type, Type> {
	
	public static final int NORMAL = 0;
	public static final int INHERITANCE = 1;
	@Deprecated
	public static final int IMPLEMENTATION = 2;
	
	private Type source;
	private Type destination;
	protected int kind = NORMAL;
	
	@Override
	public Type getSource() {
		return source;
	}
	
	@Override
	public Type getDestination() {
		return destination;
	}
	
	public int getKind() {
		return kind;
	}
	
	public TypeDependency(Type source, Type dest) {
		this.source = source;
		this.destination = dest;
	}

}
