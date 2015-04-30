package example.banking.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import example.banking.dao.AccountDao;
import example.banking.dao.InMemoryAccountDao;

@Configuration
public class BankingSpringConfiguration {

	@Bean
	public AccountDao getAccountDao() {
		return new InMemoryAccountDao();
	}
	
	@Bean
	public BankingService bankingService() {
		return new SimpleBankingService(getAccountDao());
	}
	
}
