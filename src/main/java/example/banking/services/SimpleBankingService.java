package example.banking.services;

import example.banking.dao.AccountDao;
import example.banking.domain.Account;

public class SimpleBankingService implements BankingService {

	@Override
	public void transfer(AccountDao dao, int fromAccountId, int toAccountId, double amount) {

		Account fromAccount = dao.find(fromAccountId);
		Account toAccount = dao.find(toAccountId);

		fromAccount.withdraw(amount);
		toAccount.deposit(amount);
		
		dao.update(fromAccount);
		dao.update(toAccount);

	}

}
