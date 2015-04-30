package example.pacman;

import javax.sql.DataSource;

@SuppressWarnings("unused")
public class NetworkAddrService {

	private DataSource ds = JndiHelper.jndiLookup("/jdbc/regionalDS",
			DataSource.class);

	public Object getNetworkAddr() {
		return null;
	}

}
