package example.pacman;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class JndiHelper {

	public static <T> T jndiLookup(String jndiName, Class<T> type) {
		try {
			Context naming = new InitialContext();
			return (T) naming.lookup(jndiName);
		} catch (NamingException ne) {
			ne.printStackTrace();
			return null;
		}
	}

}
