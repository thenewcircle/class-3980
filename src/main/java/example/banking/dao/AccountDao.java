package example.banking.dao;

import example.banking.domain.Account;

public interface AccountDao {
	Account create(String owner, double balance);

	Account find(int id) throws AccountNotFoundException;

	void update(Account account) throws AccountNotFoundException;
}
