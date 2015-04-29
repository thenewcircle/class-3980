package example.banking.services;

import example.banking.dao.AccountDao;

public interface BankingService {
	void transfer(AccountDao dao, int fromAccountId, int toAccountId, double amount);
}
