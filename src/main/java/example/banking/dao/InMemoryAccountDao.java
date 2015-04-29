package example.banking.dao;

import java.util.HashMap;
import java.util.Map;

import example.banking.domain.Account;

public class InMemoryAccountDao implements AccountDao {

	private static Map<Integer,Account> database = new HashMap<>();
	private int counter = 1;
	
	@Override
	public synchronized Account create(String owner, double balance) {
		int id = counter++;
		Account account = new Account(id,owner,balance);
		database.put(Integer.valueOf(id), account);
		return account;
	}

	@Override
	public Account find(int id) {
		return database.get(Integer.valueOf(id));
	}


}
