package op.production.metamorphosis.config;


import op.production.metamorphosis.model.Event;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class KafkaTopicConfig {

    public static final String EVENTS_TOPIC = "events";

    @Bean
    NewTopic eventsTopic() {
        return new NewTopic(EVENTS_TOPIC, 3, (short) 1);
    }

    @Bean
    ProducerFactory<String, Event> producerFactory(KafkaProperties kafkaProperties, JsonMapper jsonMapper) {
        var factory = new DefaultKafkaProducerFactory<String, Event>(kafkaProperties.buildProducerProperties());
        factory.setKeySerializer(new StringSerializer());
        factory.setValueSerializer(new JacksonJsonSerializer<>(jsonMapper));
        return factory;
    }

    @Bean
    KafkaTemplate<String, Event> kafkaTemplate(ProducerFactory<String, Event> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    ConsumerFactory<String, Event> consumerFactory(KafkaProperties kafkaProperties, JsonMapper jsonMapper) {
        var deserializer = new JacksonJsonDeserializer<>(Event.class, jsonMapper);
        deserializer.addTrustedPackages("op.production.metamorphosis.model");
        return new DefaultKafkaConsumerFactory<>(
                kafkaProperties.buildConsumerProperties(),
                new StringDeserializer(),
                deserializer);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Event> kafkaListenerContainerFactory(
            ConsumerFactory<String, Event> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Event>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
