package example.banking.services;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import example.banking.dao.AccountDao;
import example.banking.dao.InMemoryAccountDao;
import example.banking.domain.Account;

public class BankingServiceTest {

	private static final double ERROR_TOLERANCE = 0.00_001;

	private AccountDao dao = new InMemoryAccountDao();
	private BankingService teller = new SimpleBankingService(dao);

	@Test
	public void testHelloWorld() {
		Assert.assertEquals(1, 1);
	}

	@Test
	public void testTransfer() {

		// assemble

		// test fixtures
		double sourceBalance = 10_000_000.00;
		double targetBalance = 5.00;
		String sourceOwner = "Jane Doe";
		String targetOwner = "John Doe";
		double amount = 1_000_000.00;

		Account fromAccount = dao.create(sourceOwner, sourceBalance);
		Account toAccount = dao.create(targetOwner, targetBalance);

		int fromAccountId = fromAccount.getId();
		int toAccountId = toAccount.getId();

		// act
		teller.transfer(fromAccountId, toAccountId, amount);

		// verify
		Account finalFromAccount = dao.find(fromAccountId);
		Account finalToAccount = dao.find(toAccountId);
		Assert.assertEquals(sourceOwner, finalFromAccount.getOwner());
		Assert.assertEquals(targetOwner, finalToAccount.getOwner());
		Assert.assertEquals(sourceBalance - amount,
				finalFromAccount.getBalance(), ERROR_TOLERANCE);
		Assert.assertEquals(targetBalance + amount,
				finalToAccount.getBalance(), ERROR_TOLERANCE);

		// cleanup

	}

	@Test
	public void testAccountNotFoundInGet() {
		Assume.assumeNoException(new UnsupportedOperationException(
				"Not implemented."));
	}

	@Test
	public void testTransferWithInsufficientBalance() {
		Assume.assumeNoException(new UnsupportedOperationException(
				"Not implemented."));
	}

	@Test
	public void testAccountNotInDatabaseId() {
		Account account = new Account(null, "Jack Doe", 1_000.0);
		Assert.assertNull(account.getId());
	}

	@Test
	public void testAccountInDatabaseId() {
		Account account = dao.create("Jill Doe", 1_000.0);
		Assert.assertNotNull(account.getId());
	}

}
