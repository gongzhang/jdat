package cn.edu.buaa.sei.jdat.model;

public class BasicType extends Type {
	public static final BasicType INT = new BasicType("int");
	public static final BasicType BOOLEAN = new BasicType("boolean");
	public static final BasicType BYTE = new BasicType("byte");
	public static final BasicType CHAR = new BasicType("char");
	public static final BasicType DOUBLE = new BasicType("double");
	public static final BasicType FLOAT = new BasicType("float");
	public static final BasicType LONG = new BasicType("long");
	public static final BasicType SHORT = new BasicType("short");
	public static final BasicType VOID = new BasicType("void");
	
	private BasicType(String name) {
		super(name);
	}
	
	@Override
	public String getSignature() {
		return this.getName();
	}
	
	@Override
	public String getSimpleSignature() {
		return this.getName();
	}

	@Override
	public boolean isAbstract() {
		return false;
	}

	@Override
	public boolean isStatic() {
		return false;
	}

	@Override
	public boolean isFinal() {
		return false;
	}
	
	@Override
	public int getAccessModifiers() {
		return IModifier.PUBLIC;
	}
	
}
