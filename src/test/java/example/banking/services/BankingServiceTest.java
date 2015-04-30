package example.banking.services;

import org.junit.Assert;
import org.junit.Test;

import example.banking.dao.AccountDao;
import example.banking.dao.InMemoryAccountDao;
import example.banking.domain.Account;

public class BankingServiceTest {

	private static final double ERROR_TOLERANCE = 0.00_001;

	@Test
	public void testHelloWorld() {
		Assert.assertEquals(1, 1);
	}

	@Test
	public void testTransfer() throws AccountNotFoundException,
			InsufficientBalanceException {

		// assemble
		AccountDao dao = new InMemoryAccountDao();
		BankingService teller = new SimpleBankingService(dao);

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
		AccountDao dao = new InMemoryAccountDao();
		int nonExistingAccountId = -1;
		Account account = dao.find(nonExistingAccountId);
		Assert.assertNull(account);
	}

	@Test
	public void testAccountNotFoundStartingWithDefaultPrimaryKey() {
		AccountDao dao = new InMemoryAccountDao();
		int nonExistingAccountId = 1;
		Account account = dao.find(nonExistingAccountId);
		Assert.assertNull(account);
	}

	@Test
	public void testTransferFromNonExistingAccount()
			throws InsufficientBalanceException {

		AccountDao dao = new InMemoryAccountDao();
		BankingService teller = new SimpleBankingService(dao);

		int fromAccountId = -1;
		double amount = 1_000_000.00;
		double targetBalance = 5.00;
		String targetOwner = "John Doe";

		Account toAccount = dao.create(targetOwner, targetBalance);
		int toAccountId = toAccount.getId();

		try {
			teller.transfer(fromAccountId, toAccountId, amount);
			Assert.fail("Did not catch AccountNotFoundException.");
		} catch (AccountNotFoundException e) {
			Assert.assertNotNull(e);
			String expected = "Account #" + fromAccountId + " was not found";
			Assert.assertEquals(expected, e.getMessage());
			Assert.assertEquals(fromAccountId, e.getAccountId());
		}
	}

	@Test
	public void testTransferToNonExistingAccount()
			throws InsufficientBalanceException {

		AccountDao dao = new InMemoryAccountDao();
		BankingService teller = new SimpleBankingService(dao);

		double amount = 1_000_000.00;
		double sourceBalance = 5.00;
		String sourceOwner = "John Doe";
		int toAccountId = -1;

		Account fromAccount = dao.create(sourceOwner, sourceBalance);
		int fromAccountId = fromAccount.getId();

		try {
			teller.transfer(fromAccountId, toAccountId, amount);
			Assert.fail("Did not catch AccountNotFoundException.");
		} catch (AccountNotFoundException e) {
			Assert.assertNotNull(e);
			String expected = "Account #" + toAccountId + " was not found";
			Assert.assertEquals(expected, e.getMessage());
			Assert.assertEquals(toAccountId, e.getAccountId());
		}
	}

	@Test
	public void testTransferWithInsufficientBalance()
			throws AccountNotFoundException {

		// assemble
		AccountDao dao = new InMemoryAccountDao();
		BankingService teller = new SimpleBankingService(dao);

		// test fixture (test data)
		double sourceBalance = 10.00;
		double targetBalance = 5.00;
		String sourceOwner = "Jane Doe";
		String targetOwner = "John Doe";
		double amount = 1_000_000.00;

		Account fromAccount = dao.create(sourceOwner, sourceBalance);
		Account toAccount = dao.create(targetOwner, targetBalance);

		int fromAccountId = fromAccount.getId();
		int toAccountId = toAccount.getId();

		// act
		try {
			teller.transfer(fromAccountId, toAccountId, amount);
			Assert.fail("Did not throw InsufficientBalanceException.");
		} catch (InsufficientBalanceException e) {
			Assert.assertEquals(fromAccountId, e.getAccountId());
			Assert.assertEquals(sourceBalance, e.getBalance(), ERROR_TOLERANCE);
			Assert.assertEquals(amount, e.getWithdrawAmount(), ERROR_TOLERANCE);
			String expected = String.format(
					"Unable to withdraw %s from Account id=%s, balance=%s",
					amount, fromAccountId, fromAccount.getBalance());
			Assert.assertEquals(expected, e.getMessage());
		}

		// verify
		Account finalFrom = dao.find(fromAccountId);
		Account finalTo = dao.find(toAccountId);
		Assert.assertEquals(sourceBalance, finalFrom.getBalance(),
				ERROR_TOLERANCE);
		Assert.assertEquals(targetBalance, finalTo.getBalance(),
				ERROR_TOLERANCE);

		// cleanup

	}

	@Test
	public void testAccountNotInDatabaseId() {
		Account account = new Account(null, "Jack Doe", 1_000.0);
		Assert.assertNull(account.getId());
	}

	@Test
	public void testAccountInDatabaseId() {
		AccountDao dao = new InMemoryAccountDao();
		Account account = dao.create("Jill Doe", 1_000.0);
		Assert.assertNotNull(account.getId());
	}

}
