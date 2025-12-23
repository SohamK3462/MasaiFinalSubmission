package com.example.EventService.Config;

import com.example.EventService.Dto.EventRequest;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import static org.assertj.core.api.Assertions.assertThat;

class KafkaProducerConfigTest {

    private final KafkaProducerConfig config = new KafkaProducerConfig();

    @Test
    void producerFactory_ShouldReturnNonNullFactory() {
        // Act
        DefaultKafkaProducerFactory<String, EventRequest> factory = config.producerFactory();

        // Assert
        assertThat(factory).isNotNull();
    }

    @Test
    void producerFactory_ShouldConfigureBootstrapServers() {
        // Act
        DefaultKafkaProducerFactory<String, EventRequest> factory = config.producerFactory();
        var configMap = factory.getConfigurationProperties();

        // Assert
        assertThat(configMap).containsKey(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG);
        assertThat(configMap.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG)).isEqualTo("localhost:29092");
    }

    @Test
    void producerFactory_ShouldConfigureKeySerializer() {
        // Act
        DefaultKafkaProducerFactory<String, EventRequest> factory = config.producerFactory();
        var configMap = factory.getConfigurationProperties();

        // Assert
        assertThat(configMap).containsKey(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG);
        assertThat(configMap.get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG)).isEqualTo(StringSerializer.class);
    }

    @Test
    void producerFactory_ShouldConfigureValueSerializer() {
        // Act
        DefaultKafkaProducerFactory<String, EventRequest> factory = config.producerFactory();
        var configMap = factory.getConfigurationProperties();

        // Assert
        assertThat(configMap).containsKey(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG);
        assertThat(configMap.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)).isEqualTo(JsonSerializer.class);
    }

    @Test
    void producerFactory_ShouldConfigureJsonSerializerTypeInfo() {
        // Act
        DefaultKafkaProducerFactory<String, EventRequest> factory = config.producerFactory();
        var configMap = factory.getConfigurationProperties();

        // Assert
        assertThat(configMap).containsKey(JsonSerializer.ADD_TYPE_INFO_HEADERS);
        assertThat(configMap.get(JsonSerializer.ADD_TYPE_INFO_HEADERS)).isEqualTo(false);
    }

    @Test
    void kafkaTemplate_ShouldReturnNonNullTemplate() {
        // Act
        KafkaTemplate<String, EventRequest> template = config.kafkaTemplate();

        // Assert
        assertThat(template).isNotNull();
    }

    @Test
    void kafkaTemplate_ShouldUseProducerFactory() {
        // Act
        KafkaTemplate<String, EventRequest> template = config.kafkaTemplate();

        // Assert
        assertThat(template.getProducerFactory()).isNotNull();
    }

    @Test
    void producerFactory_ShouldHaveCorrectNumberOfConfigs() {
        // Act
        DefaultKafkaProducerFactory<String, EventRequest> factory = config.producerFactory();
        var configMap = factory.getConfigurationProperties();

        // Assert - should have bootstrap servers, key serializer, value serializer, and json type info
        assertThat(configMap).hasSize(4);
    }

    @Test
    void producerFactory_ShouldBeReusable() {
        // Act
        DefaultKafkaProducerFactory<String, EventRequest> factory1 = config.producerFactory();
        DefaultKafkaProducerFactory<String, EventRequest> factory2 = config.producerFactory();

        // Assert - each call creates a new instance but with same config
        assertThat(factory1).isNotSameAs(factory2);
        assertThat(factory1.getConfigurationProperties())
                .isEqualTo(factory2.getConfigurationProperties());
    }

    @Test
    void kafkaTemplate_ShouldBeReusable() {
        // Act
        KafkaTemplate<String, EventRequest> template1 = config.kafkaTemplate();
        KafkaTemplate<String, EventRequest> template2 = config.kafkaTemplate();

        // Assert - each call creates a new instance
        assertThat(template1).isNotSameAs(template2);
    }
}

