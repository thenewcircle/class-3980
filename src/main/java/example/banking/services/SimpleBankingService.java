package example.banking.services;

import example.banking.dao.AccountDao;
import example.banking.domain.Account;

public class SimpleBankingService implements BankingService {

	private AccountDao dao;

	// constructor injection
	public SimpleBankingService(AccountDao dao) {
		this.dao = dao;
	}

	// setter injection
	public void setAccountDao(AccountDao dao) {
		this.dao = dao;
	}

	@Override
	public void transfer(int fromAccountId, int toAccountId, double amount) {

		Account fromAccount = dao.find(fromAccountId);
		Account toAccount = dao.find(toAccountId);

		fromAccount.withdraw(amount);
		toAccount.deposit(amount);

		dao.update(fromAccount);
		dao.update(toAccount);

	}

}
