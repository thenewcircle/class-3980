package example.banking.dao;

import java.util.HashMap;
import java.util.Map;

import example.banking.domain.Account;

public class InMemoryAccountDao implements AccountDao {

	private Map<Integer, Account> database = new HashMap<>();
	private int counter = 1;

	public InMemoryAccountDao() {
		super();
	}

	@Override
	public synchronized Account create(String owner, double balance) {
		int id = counter++;
		Account account = new Account(id, owner, balance);
		database.put(Integer.valueOf(id), account);
		return new Account(account.getId(), account.getOwner(),
				account.getBalance());
	}

	@Override
	public Account find(int id) throws AccountNotFoundException {
		Account account = database.get(Integer.valueOf(id));
		if (account == null)
			throw new AccountNotFoundException(id);
		return new Account(account.getId(), account.getOwner(),
				account.getBalance());
	}

	@Override
	public void update(Account account) throws AccountNotFoundException {
		if (account.getId() == null)
			throw new IllegalArgumentException("Account id cannot be null");
		if (!database.containsKey(account.getId()))
			throw new AccountNotFoundException(account.getId());
		database.put(account.getId(), account);
	}

}
