package example.banking.dao;

import java.util.HashMap;
import java.util.Map;

import example.banking.domain.Account;

public class InMemoryAccountDao implements AccountDao {

	private static Map<Integer,Account> database = new HashMap<>();
	
	@Override
	public synchronized Account create(int id, double balance) {
		Account account = new Account(id,balance);
		database.put(Integer.valueOf(id), account);
		return account;
	}

	@Override
	public Account find(int id) {
		return database.get(Integer.valueOf(id));
	}

}
