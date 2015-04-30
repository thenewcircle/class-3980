package example.pacman;

import java.util.ArrayList;
import java.util.List;

public class MyValidator {

	private CustomerService customerService;
	private NetworkAddrService networkService;
	private RequestMessageLogService requestMessageLogService;

	public MyValidator(CustomerService customerService,
			NetworkAddrService networkService,
			RequestMessageLogService requestMessageLogService) {
		this.customerService = customerService;
		this.networkService = networkService;
		this.requestMessageLogService = requestMessageLogService;
	}

	@SuppressWarnings("unused")
	public List<ValidationError> validate(RequestMessage request) {
		Object customer = customerService.findCustomer();
		Object network = networkService.getNetworkAddr();
		List<ValidationError> errors = new ArrayList<>();
		for (String name : request.getCustomerNames()) {
			ValidationError e = new ValidationError(name, 7032);
			errors.add(e);
		}
		for (ValidationError error : errors) {
			requestMessageLogService.log(error);
		}
		return errors;
	}

}
