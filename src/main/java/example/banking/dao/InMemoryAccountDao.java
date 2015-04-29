package example.banking.dao;

import java.util.HashMap;
import java.util.Map;

import example.banking.domain.Account;

public class InMemoryAccountDao implements AccountDao {

	private static Map<Integer, Account> database = new HashMap<>();
	private int counter = 1;

	@Override
	public synchronized Account create(String owner, double balance) {
		int id = counter++;
		Account account = new Account(id, owner, balance);
		database.put(Integer.valueOf(id), account);
		return new Account(account.getId(), account.getOwner(),
				account.getBalance());
	}

	@Override
	public Account find(int id) {
		Account account = database.get(Integer.valueOf(id));
		return new Account(account.getId(), account.getOwner(),
				account.getBalance());
	}

	@Override
	public void update(Account account) {
		if (account.getId() == null)
			throw new RuntimeException("Not found.");
		Account found = database.get(account.getId());
		if (found == null)
			throw new RuntimeException("Not found!");
		database.put(account.getId(), account);
	}

}
