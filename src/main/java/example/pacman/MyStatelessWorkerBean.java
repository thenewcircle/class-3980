package example.pacman;

import java.util.List;

import javax.ejb.Stateless;

@Stateless
public class MyStatelessWorkerBean {

	public void doWork(RequestMessage request) {
		ConfigurationService configuration = ConfigurationService.getInstance();
		MyValidator validator = configuration.getValidator();
		List<ValidationError> errors = validator.validate(request);
		request.setErrors(errors);
		DistributionEngine de = configuration.getDistributionEngine();
		de.buildMessages(request);
	}

}
