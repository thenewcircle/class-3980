package example.banking.services;

import example.banking.dao.AccountDao;
import example.banking.dao.InMemoryAccountDao;
import example.banking.domain.Account;

public class SimpleBankingService implements BankingService {

	@Override
	public void transfer(int fromAccountId, int toAccountId, double amount) {
		
		AccountDao dao = new InMemoryAccountDao();
		
		Account fromAccount = dao.find(fromAccountId);
		Account toAccount = dao.find(toAccountId);
		
		fromAccount.setBalance(fromAccount.getBalance()-amount);
		toAccount.setBalance(toAccount.getBalance()+amount);
		
	}

}