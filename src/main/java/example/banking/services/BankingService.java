package example.banking.services;

import example.banking.dao.AccountNotFoundException;

public interface BankingService {
	void transfer(int fromAccountId, int toAccountId, double amount)
			throws AccountNotFoundException, InsufficientBalanceException;
}
