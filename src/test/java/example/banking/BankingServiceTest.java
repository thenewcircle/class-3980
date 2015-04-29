package example.banking;

import org.junit.Assert;
import org.junit.Test;

public class BankingServiceTest {

	private static final double ERROR_TOLERANCE = 0.00_001;

	@Test
	public void testHelloWorld() {
		Assert.assertEquals(1, 1);
	}

	@Test
	public void testTransfer() {

		// assemble
		AccountDao dao = new InMemoryAccountDao();
		BankingService teller = new SimpleBankingService();

		// test fixtures
		int sourceId = 1;
		int targetId = 2;
		double sourceBalance = 10_000_000.00;
		double targetBalance = 5.00;
		double amount = 1_000_000.00;

		Account fromAccount = dao.create(sourceId, sourceBalance);
		Account toAccount = dao.create(targetId, targetBalance);

		int fromAccountId = fromAccount.getId();
		int toAccountId = toAccount.getId();

		// act
		teller.transfer(fromAccountId, toAccountId, amount);

		// verify
		Account finalFromAccount = dao.find(fromAccountId);
		Account finalToAccount = dao.find(toAccountId);
		Assert.assertEquals(sourceBalance - amount, finalFromAccount.getBalance(), ERROR_TOLERANCE);
		Assert.assertEquals(targetBalance + amount, finalToAccount.getBalance(), ERROR_TOLERANCE);

		// cleanup

	}

}
