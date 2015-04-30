package example.pacman;

import javax.sql.DataSource;

@SuppressWarnings("unused")
public class NetworkAddrService {
	
	private DataSource datasource;
	
	public NetworkAddrService(DataSource datasource) {
		this.datasource = datasource;
	}

	public Object getNetworkAddr() {
		return null;
	}

}
