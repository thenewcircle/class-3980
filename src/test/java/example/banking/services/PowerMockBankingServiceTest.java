package example.banking.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import example.banking.dao.AccountDao;
import example.banking.dao.AccountNotFoundException;
import example.banking.dao.InMemoryAccountDao;
import example.banking.domain.Account;

@RunWith(PowerMockRunner.class)
public class PowerMockBankingServiceTest {

	private static final double ERROR_TOLERANCE = 0.00_001;

	@Test
	@PrepareForTest(ConfigurationService.class)
	public void testTransfer() throws AccountNotFoundException,
			InsufficientBalanceException {

		// assemble
		AccountDao dummyDao = new InMemoryAccountDao();
		BankingService dummyTeller = new SimpleBankingService(dummyDao);

		mockStatic(ConfigurationService.class);
		when(ConfigurationService.getAccountDao()).thenReturn(dummyDao);
		when(ConfigurationService.getBankingService()).thenReturn(dummyTeller);

		// now powermock will substitute static calls with our objects
		AccountDao dao = ConfigurationService.getAccountDao();
		BankingService teller = ConfigurationService.getBankingService();

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
		assertEquals(sourceOwner, finalFromAccount.getOwner());
		assertEquals(targetOwner, finalToAccount.getOwner());
		assertEquals(sourceBalance - amount, finalFromAccount.getBalance(),
				ERROR_TOLERANCE);
		assertEquals(targetBalance + amount, finalToAccount.getBalance(),
				ERROR_TOLERANCE);

		// cleanup
	}

	@Test
	@PrepareForTest(ConfigurationService.class)
	public void testTransferFromNonExistingAccount()
			throws InsufficientBalanceException {

		AccountDao dummyDao = new InMemoryAccountDao();
		BankingService dummyTeller = new SimpleBankingService(dummyDao);

		mockStatic(ConfigurationService.class);
		when(ConfigurationService.getAccountDao()).thenReturn(dummyDao);
		when(ConfigurationService.getBankingService()).thenReturn(dummyTeller);

		AccountDao dao = ConfigurationService.getAccountDao();
		BankingService teller = ConfigurationService.getBankingService();

		int fromAccountId = -1;
		double amount = 1_000_000.00;
		double targetBalance = 5.00;
		String targetOwner = "John Doe";

		Account toAccount = dao.create(targetOwner, targetBalance);
		int toAccountId = toAccount.getId();

		try {
			teller.transfer(fromAccountId, toAccountId, amount);
			fail("Did not catch AccountNotFoundException.");
		} catch (AccountNotFoundException e) {
			assertNotNull(e);
			String expected = "Account #" + fromAccountId + " was not found";
			assertEquals(expected, e.getMessage());
			assertEquals(fromAccountId, e.getAccountId());
		}
	}

	@Test
	@PrepareForTest(ConfigurationService.class)
	public void testTransferToNonExistingAccount()
			throws InsufficientBalanceException {

		AccountDao dummyDao = new InMemoryAccountDao();
		BankingService dummyTeller = new SimpleBankingService(dummyDao);

		mockStatic(ConfigurationService.class);
		when(ConfigurationService.getAccountDao()).thenReturn(dummyDao);
		when(ConfigurationService.getBankingService()).thenReturn(dummyTeller);

		AccountDao dao = ConfigurationService.getAccountDao();
		BankingService teller = ConfigurationService.getBankingService();

		double amount = 1_000_000.00;
		double sourceBalance = 5.00;
		String sourceOwner = "John Doe";
		int toAccountId = -1;

		Account fromAccount = dao.create(sourceOwner, sourceBalance);
		int fromAccountId = fromAccount.getId();

		try {
			teller.transfer(fromAccountId, toAccountId, amount);
			fail("Did not catch AccountNotFoundException.");
		} catch (AccountNotFoundException e) {
			assertNotNull(e);
			String expected = "Account #" + toAccountId + " was not found";
			assertEquals(expected, e.getMessage());
			assertEquals(toAccountId, e.getAccountId());
		}
	}

	@Test
	@PrepareForTest(ConfigurationService.class)
	public void testTransferWithInsufficientBalance()
			throws AccountNotFoundException {

		// assemble
		AccountDao dummyDao = new InMemoryAccountDao();
		BankingService dummyTeller = new SimpleBankingService(dummyDao);

		mockStatic(ConfigurationService.class);
		when(ConfigurationService.getAccountDao()).thenReturn(dummyDao);
		when(ConfigurationService.getBankingService()).thenReturn(dummyTeller);

		AccountDao dao = ConfigurationService.getAccountDao();
		BankingService teller = ConfigurationService.getBankingService();

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
			fail("Did not throw InsufficientBalanceException.");
		} catch (InsufficientBalanceException e) {
			assertEquals(fromAccountId, e.getAccountId());
			assertEquals(sourceBalance, e.getBalance(), ERROR_TOLERANCE);
			assertEquals(amount, e.getWithdrawAmount(), ERROR_TOLERANCE);
			String expected = String.format(
					"Unable to withdraw %s from Account id=%s, balance=%s",
					amount, fromAccountId, fromAccount.getBalance());
			assertEquals(expected, e.getMessage());
		}

		// verify
		Account finalFrom = dao.find(fromAccountId);
		Account finalTo = dao.find(toAccountId);
		assertEquals(sourceBalance, finalFrom.getBalance(), ERROR_TOLERANCE);
		assertEquals(targetBalance, finalTo.getBalance(), ERROR_TOLERANCE);

		// cleanup

	}

}
