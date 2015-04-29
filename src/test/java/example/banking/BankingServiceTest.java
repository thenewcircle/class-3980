package example.banking;

import org.junit.Assert;
import org.junit.Test;

public class BankingServiceTest {

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
		Account fromAccount = dao.create(1, 10_000_000.00);
		Account toAccount = dao.create(2, 5.00);
		int fromAccountId = fromAccount.getId();
		int toAccountId = toAccount.getId();
		double amount = 1_000_000.00;

		// act
		teller.transfer(fromAccountId, toAccountId, amount);

		// verify
		Account finalFromAccount = dao.find(fromAccountId);
		Account finalToAccount = dao.find(toAccountId);
		Assert.assertEquals(10_000_000.0 - 1_000_000.0, finalFromAccount.getBalance());
		Assert.assertEquals(5.0 + 1_000_000.0, finalToAccount.getBalance());

		// cleanup

	}

}
