package example.pacman;

import javax.sql.DataSource;

@SuppressWarnings("unused")
public class CustomerService {

	private DataSource ds = JndiHelper.jndiLookup("/jdbc/regionalDS", DataSource.class);

	public Object findCustomer() {
		return null;
	}

}
