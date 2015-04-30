package example.banking.services;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import example.banking.dao.AccountDao;
import example.banking.dao.AccountNotFoundException;
import example.banking.domain.Account;

public class MockitoBankingServiceTest {

	private static final double ERROR_TOLERANCE = 0.00_001;

	@Test
	public void testTransfer() throws AccountNotFoundException,
			InsufficientBalanceException {

		AccountDao dao = Mockito.mock(AccountDao.class);
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

		Mockito.when(dao.find(fromId)).thenReturn(fromAccount);
		Mockito.when(dao.find(toId)).thenReturn(toAccount);

		teller.transfer(fromId, toId, amount);

		Mockito.verify(dao).find(fromId);
		Mockito.verify(dao).find(toId);

		Mockito.verify(dao).update(fromAccount);
		Mockito.verify(dao).update(toAccount);

		Account finalFromAccount = dao.find(fromId);
		Account finalToAccount = dao.find(toId);
		Assert.assertEquals(fromOwner, finalFromAccount.getOwner());
		Assert.assertEquals(toOwner, finalToAccount.getOwner());
		Assert.assertEquals(fromBalance - amount,
				finalFromAccount.getBalance(), ERROR_TOLERANCE);
		Assert.assertEquals(toBalance + amount, finalToAccount.getBalance(),
				ERROR_TOLERANCE);

		// cleanup
	}

	@Test
	public void testTransferFromNonExistingAccount()
			throws InsufficientBalanceException, AccountNotFoundException {

		AccountDao dao = Mockito.mock(AccountDao.class);
		BankingService teller = new SimpleBankingService(dao);

		int fromId = -1;

		double toBalance = 5.00;
		String toOwner = "John Doe";
		int toId = 1;

		double amount = 1_000_000.00;

		Account toAccount = new Account(toId, toOwner, toBalance);

		Mockito.when(dao.find(fromId)).thenThrow(new AccountNotFoundException(fromId));

		try {
			teller.transfer(fromId, toId, amount);
			Assert.fail("Did not catch AccountNotFoundException.");
		} catch (AccountNotFoundException e) {
			Assert.assertNotNull(e);
			String expected = "Account #" + fromId + " was not found";
			Assert.assertEquals(expected, e.getMessage());
			Assert.assertEquals(fromId, e.getAccountId());
		}

		Mockito.verify(dao).find(fromId);

	}

	@Test
	public void testTransferToNonExistingAccount()
			throws InsufficientBalanceException, AccountNotFoundException {

		AccountDao dao = Mockito.mock(AccountDao.class);
		BankingService teller = new SimpleBankingService(dao);

		double fromBalance = 5.00;
		String fromOwner = "John Doe";
		int fromId = 1;

		int toId = -1;

		double amount = 1_000_000.00;

		Account fromAccount = new Account(fromId, fromOwner, fromBalance);
		Mockito.when(dao.find(fromId)).thenReturn(fromAccount);
		Mockito.when(dao.find(toId)).thenThrow(new AccountNotFoundException(toId));

		try {
			teller.transfer(fromId, toId, amount);
			Assert.fail("Did not catch AccountNotFoundException.");
		} catch (AccountNotFoundException e) {
			Assert.assertNotNull(e);
			String expected = "Account #" + toId + " was not found";
			Assert.assertEquals(expected, e.getMessage());
			Assert.assertEquals(toId, e.getAccountId());
		}

		Mockito.verify(dao).find(fromId);
		Mockito.verify(dao).find(toId);

	}

	@Test
	public void testTransferWithInsufficientBalance()
			throws AccountNotFoundException {

		AccountDao dao = Mockito.mock(AccountDao.class);
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

		Mockito.when(dao.find(fromId)).thenReturn(fromAccount);
		Mockito.when(dao.find(toId)).thenReturn(toAccount);

		try {
			teller.transfer(fromId, toId, amount);
			Assert.fail("Did not throw InsufficientBalanceException.");
		} catch (InsufficientBalanceException e) {
			Assert.assertEquals(fromId, e.getAccountId());
			Assert.assertEquals(fromBalance, e.getBalance(), ERROR_TOLERANCE);
			Assert.assertEquals(amount, e.getWithdrawAmount(), ERROR_TOLERANCE);
			String expected = String.format(
					"Unable to withdraw %s from Account id=%s, balance=%s",
					amount, fromId, fromAccount.getBalance());
			Assert.assertEquals(expected, e.getMessage());
		}


		Mockito.verify(dao).find(fromId);
		Mockito.verify(dao).find(toId);

		Account finalFromAccount = dao.find(fromId);
		Account finalToAccount = dao.find(toId);
		Assert.assertEquals(fromBalance, finalFromAccount.getBalance(),
				ERROR_TOLERANCE);
		Assert.assertEquals(toBalance, finalToAccount.getBalance(),
				ERROR_TOLERANCE);

	}

}
