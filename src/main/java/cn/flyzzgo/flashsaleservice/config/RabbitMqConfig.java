package cn.flyzzgo.flashsaleservice.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author Flyzz
 */
@Configuration
@EnableRabbit
public class RabbitMqConfig {

    @Bean("orderTaskExchange")
    public Exchange flashOrderTaskExchange() {
        return ExchangeBuilder.topicExchange("order.task").build();
    }

    @Bean
    public DirectMessageListenerContainer directMessageListenerContainer() {
        DirectMessageListenerContainer container = new DirectMessageListenerContainer();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(16);
        executor.setMaxPoolSize(20);
        executor.setThreadNamePrefix("flashOrderTask ");
        executor.afterPropertiesSet();
        container.setTaskExecutor(executor);
        container.setConnectionFactory(connectionFactory());
        return container;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setPublisherReturns(true);
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        return connectionFactory;
    }


    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
