package example.banking;

public interface BankingService {
	void transfer(int fromAccountId, int toAccountId, double amount);
}
