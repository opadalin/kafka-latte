package op.production.metamorphosis.consumer;

import op.production.metamorphosis.config.KafkaTopicConfig;
import op.production.metamorphosis.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);

    @KafkaListener(topics = KafkaTopicConfig.EVENTS_TOPIC)
    public void consume(Event event) {
        log.info("Consumed event: [id={}, source={}, type={}, payload={}]",
                event.id(), event.source(), event.type(), event.payload());
    }
}
