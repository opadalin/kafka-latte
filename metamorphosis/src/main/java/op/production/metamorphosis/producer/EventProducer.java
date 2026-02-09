package op.production.metamorphosis.producer;

import op.production.metamorphosis.config.KafkaTopicConfig;
import op.production.metamorphosis.model.Event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

	private final KafkaTemplate<String, Event> kafkaTemplate;

	public EventProducer(KafkaTemplate<String, Event> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void send(Event event) {
		kafkaTemplate.send(KafkaTopicConfig.EVENTS_TOPIC, event.id(), event);
	}
}
