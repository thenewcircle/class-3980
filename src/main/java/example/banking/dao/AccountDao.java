package example.banking.dao;

import example.banking.domain.Account;

public interface AccountDao {
	Account create(int id, String owner, double balance);
	Account find(int id);
}
