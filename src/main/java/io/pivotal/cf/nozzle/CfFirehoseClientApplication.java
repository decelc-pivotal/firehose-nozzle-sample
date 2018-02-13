package io.pivotal.cf.nozzle;

import java.util.UUID;

import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.FirehoseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class CfFirehoseClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(CfFirehoseClientApplication.class, args);
	}

}


@Component
class SampleCommandLineRunner implements CommandLineRunner {

	private final DopplerClient dopplerClient;

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleCommandLineRunner.class);

	@Autowired
	public SampleCommandLineRunner(DopplerClient dopplerClient) {
		this.dopplerClient = dopplerClient;
	}

	@Override
	public void run(String... args) throws Exception {

		Flux<Envelope> cfEvents = this.dopplerClient.firehose(
				FirehoseRequest
						.builder()
						.subscriptionId(UUID.randomUUID().toString()).build());

		
		cfEvents.subscribe(e -> LOGGER.info(e.toString()));

		// cfEvents.filter(e ->
		// e.getOrigin().equals("metrics-forwarder")).filter(e ->
		// e.getEventType().equals(EventType.VALUE_METRIC)).filter(e ->
		// e.getValueMetric().getName().contains("cjd-autoscaler-metric"))
		// .subscribe(e -> LOGGER.info("value metric=" + e.getValueMetric() ));
	}

}
