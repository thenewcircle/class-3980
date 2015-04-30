package example.pacman;

import javax.sql.DataSource;

public class SecurityService {

	private DataSource datasource;

	public SecurityService(DataSource datasource) {
		this.datasource = datasource;
	}

}
