package example.banking.services;

import example.banking.dao.AccountDao;
import example.banking.dao.InMemoryAccountDao;

public class ConfigurationService {

	private static AccountDao accountDao = new InMemoryAccountDao();
	private static BankingService bankingService = new SimpleBankingService();
	
	public static AccountDao getAccountDao() {
		return accountDao;
	}
	
	public static BankingService getBankingService() {
		return bankingService;
	}
	
}
