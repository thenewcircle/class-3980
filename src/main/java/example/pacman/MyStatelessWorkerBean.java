package example.pacman;

import java.util.List;

import javax.ejb.Stateless;

@Stateless
public class MyStatelessWorkerBean {

	public void doWork(RequestMessage request) {
		MyValidator validator = new MyValidator();
		List<ValidationError> errors = validator.validate(request);
		request.setErrors(errors);
		DistributionEngine de = new DistributionEngine();
		de.buildMessages(request);
	}

}
