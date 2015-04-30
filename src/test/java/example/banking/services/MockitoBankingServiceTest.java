package example.banking.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import example.banking.dao.AccountDao;
import example.banking.dao.AccountNotFoundException;
import example.banking.domain.Account;

public class MockitoBankingServiceTest {

	private static final double ERROR_TOLERANCE = 0.00_001;

	@Test
	public void testTransfer() throws AccountNotFoundException,
			InsufficientBalanceException {

		AccountDao dao = mock(AccountDao.class);
		BankingService teller = new SimpleBankingService(dao);

		int fromId = 1;
		double fromBalance = 10_000_000.00;
		String fromOwner = "Jane Doe";

		int toId = 2;
		double toBalance = 5.00;
		String toOwner = "John Doe";

		double amount = 1_000_000.00;

		Account fromAccount = new Account(fromId, fromOwner, fromBalance);
		Account toAccount = new Account(toId, toOwner, toBalance);

		when(dao.find(fromId)).thenReturn(fromAccount);
		when(dao.find(toId)).thenReturn(toAccount);

		teller.transfer(fromId, toId, amount);

		verify(dao).find(fromId);
		verify(dao).find(toId);

		verify(dao).update(fromAccount);
		verify(dao).update(toAccount);

		Account finalFromAccount = dao.find(fromId);
		Account finalToAccount = dao.find(toId);
		assertEquals(fromOwner, finalFromAccount.getOwner());
		assertEquals(toOwner, finalToAccount.getOwner());
		assertEquals(fromBalance - amount, finalFromAccount.getBalance(),
				ERROR_TOLERANCE);
		assertEquals(toBalance + amount, finalToAccount.getBalance(),
				ERROR_TOLERANCE);

		// cleanup
	}

	@Test
	public void testTransferFromNonExistingAccount()
			throws InsufficientBalanceException, AccountNotFoundException {

		AccountDao dao = mock(AccountDao.class);
		BankingService teller = new SimpleBankingService(dao);

		int fromId = -1;
		int toId = 1;

		double amount = 1_000_000.00;

		when(dao.find(fromId)).thenThrow(new AccountNotFoundException(fromId));

		try {
			teller.transfer(fromId, toId, amount);
			fail("Did not catch AccountNotFoundException.");
		} catch (AccountNotFoundException e) {
			assertNotNull(e);
			String expected = "Account #" + fromId + " was not found";
			assertEquals(expected, e.getMessage());
			assertEquals(fromId, e.getAccountId());
		}

		verify(dao).find(fromId);

	}

	@Test
	public void testTransferToNonExistingAccount()
			throws InsufficientBalanceException, AccountNotFoundException {

		AccountDao dao = mock(AccountDao.class);
		BankingService teller = new SimpleBankingService(dao);

		double fromBalance = 5.00;
		String fromOwner = "John Doe";
		int fromId = 1;

		int toId = -1;

		double amount = 1_000_000.00;

		Account fromAccount = new Account(fromId, fromOwner, fromBalance);
		when(dao.find(fromId)).thenReturn(fromAccount);
		when(dao.find(toId)).thenThrow(
				new AccountNotFoundException(toId));

		try {
			teller.transfer(fromId, toId, amount);
			fail("Did not catch AccountNotFoundException.");
		} catch (AccountNotFoundException e) {
			assertNotNull(e);
			String expected = "Account #" + toId + " was not found";
			assertEquals(expected, e.getMessage());
			assertEquals(toId, e.getAccountId());
		}

		verify(dao).find(fromId);
		verify(dao).find(toId);

	}

	@Test
	public void testTransferWithInsufficientBalance()
			throws AccountNotFoundException {

		AccountDao dao = mock(AccountDao.class);
		BankingService teller = new SimpleBankingService(dao);

		int fromId = 1;
		double fromBalance = 10.00;
		String fromOwner = "Jane Doe";

		int toId = 2;
		double toBalance = 5.00;
		String toOwner = "John Doe";

		double amount = 1_000_000.00;

		Account fromAccount = new Account(fromId, fromOwner, fromBalance);
		Account toAccount = new Account(toId, toOwner, toBalance);

		when(dao.find(fromId)).thenReturn(fromAccount);
		when(dao.find(toId)).thenReturn(toAccount);

		try {
			teller.transfer(fromId, toId, amount);
			fail("Did not throw InsufficientBalanceException.");
		} catch (InsufficientBalanceException e) {
			assertEquals(fromId, e.getAccountId());
			assertEquals(fromBalance, e.getBalance(), ERROR_TOLERANCE);
			assertEquals(amount, e.getWithdrawAmount(), ERROR_TOLERANCE);
			String expected = String.format(
					"Unable to withdraw %s from Account id=%s, balance=%s",
					amount, fromId, fromAccount.getBalance());
			assertEquals(expected, e.getMessage());
		}

		verify(dao).find(fromId);
		verify(dao).find(toId);

		Account finalFromAccount = dao.find(fromId);
		Account finalToAccount = dao.find(toId);
		assertEquals(fromBalance, finalFromAccount.getBalance(),
				ERROR_TOLERANCE);
		assertEquals(toBalance, finalToAccount.getBalance(),
				ERROR_TOLERANCE);

	}

}
