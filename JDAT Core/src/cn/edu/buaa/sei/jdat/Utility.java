package cn.edu.buaa.sei.jdat;

public final class Utility {
	public static String getPackageName(String signature) {
		int loc = signature.lastIndexOf('.');
		if (loc == -1)
			return signature;
		return signature.substring(0, loc);
	}
	
	public static String getTypeName(String signature) {
		int loc = signature.lastIndexOf('.');
		if (loc == -1)
			return signature;
		return signature.substring(loc + 1);
	}
}
