package example.banking.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import example.banking.dao.AccountDao;
import example.banking.dao.AccountNotFoundException;
import example.banking.domain.Account;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=BankingSpringConfiguration.class)
public class SpringBankingServiceTest {

	private static final double ERROR_TOLERANCE = 0.00_001;

	@Autowired
	private AccountDao dao;

	@Resource(name = "bankingService")
	private BankingService teller;

	@Test
	public void testTransfer() throws AccountNotFoundException,
			InsufficientBalanceException {

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
		assertEquals(sourceOwner, finalFromAccount.getOwner());
		assertEquals(targetOwner, finalToAccount.getOwner());
		assertEquals(sourceBalance - amount, finalFromAccount.getBalance(),
				ERROR_TOLERANCE);
		assertEquals(targetBalance + amount, finalToAccount.getBalance(),
				ERROR_TOLERANCE);

		// cleanup
	}

	@Test
	public void testTransferFromNonExistingAccount()
			throws InsufficientBalanceException {

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
	public void testTransferToNonExistingAccount()
			throws InsufficientBalanceException {

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
	public void testTransferWithInsufficientBalance()
			throws AccountNotFoundException {

		// assemble

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
