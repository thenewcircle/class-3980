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
		BankingService teller = new SimpleBankingService();
		teller.transfer(1, 2, 1_000_000.00);
	}

}
