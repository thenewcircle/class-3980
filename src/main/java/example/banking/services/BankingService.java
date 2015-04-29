package example.banking.services;

public interface BankingService {
	void transfer(int fromAccountId, int toAccountId, double amount);
}
