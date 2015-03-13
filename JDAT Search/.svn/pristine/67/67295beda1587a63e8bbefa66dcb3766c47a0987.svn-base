package cn.edu.buaa.sei.jdat.search;

public class Utility {
	
	/**
	 * Split domain name by dots. For example, a name-space "com.a.b", the return should be
	 * [com, a, b]. The empty string wouldn't be overlooked.
	 * @param domain
	 * @return
	 */
	public static String[] splitDomain(String domain) {
		int dot = 0;
		for (int i = 0; i < domain.length(); i++) {
			if (domain.charAt(i) == '.')
				dot++;
		}
		if (dot == 0)
			return new String[]{domain};
		String[] tokens = new String[dot + 1];
		StringBuilder sb = new StringBuilder();
		dot = 0;
		for (int i = 0; i < domain.length(); i++) {
			char ch = domain.charAt(i);
			if (ch == '.') {
				tokens[dot++] = sb.toString();
				sb.setLength(0);
			}
			else
				sb.append(ch);
		}
		tokens[dot] = sb.toString();
		return tokens;
	}

}
