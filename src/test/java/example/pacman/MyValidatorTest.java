package example.pacman;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MyValidatorTest {

	@Test
	public void testMyValidator() {

		// assemble
		CustomerService customerService = Mockito.mock(CustomerService.class);
		NetworkAddrService networkService = Mockito
				.mock(NetworkAddrService.class);
		RequestMessageLogService requestMessageLogService = Mockito
				.mock(RequestMessageLogService.class);
		MyValidator validator = new MyValidator(customerService,
				networkService, requestMessageLogService);

		// test fixture
		String text = "Jacky,Jose,Lei,Mazher,Neven";
		RequestMessage requestMessage = RequestMessage.parse(text);
		List<ValidationError> expectedErrors = Arrays.asList(
				new ValidationError("Jacky", 7032), new ValidationError("Jose",
						7032), new ValidationError("Lei", 7032),
				new ValidationError("Mazher", 7032), new ValidationError(
						"Neven", 7032));

		// act
		List<ValidationError> actualErrors = validator.validate(requestMessage);

		// verify
		Assert.assertEquals(expectedErrors, actualErrors);
		Mockito
			.verify(requestMessageLogService, Mockito.times(expectedErrors.size()))
			.log(Mockito.any(ValidationError.class));

		// cleanup

	}

}
