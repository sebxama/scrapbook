package core.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import core.activation.ActivationController;
import core.aggregation.AggregationController;
import core.alignment.AlignmentController;

@SpringBootApplication
@ComponentScan(basePackageClasses = { AggregationController.class, AlignmentController.class, ActivationController.class })
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

}
