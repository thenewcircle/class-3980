package example.pacman;

import javax.sql.DataSource;

@SuppressWarnings("unused")
public class CustomerService {

	private DataSource datasource;

	public CustomerService(DataSource datasource) {
		this.datasource = datasource;
	}

	public Object findCustomer() {
		return null;
	}

}
