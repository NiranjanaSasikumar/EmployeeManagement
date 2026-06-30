package com.example.EmployeManagement.Redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("employee-created-channel");
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(EmployeeListener listener) {

        MessageListenerAdapter adapter =
                new MessageListenerAdapter(listener, "receiveMessage");

        adapter.setSerializer(new org.springframework.data.redis.serializer.StringRedisSerializer());

        return adapter;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter,
            ChannelTopic topic) {

        RedisMessageListenerContainer container =
                new RedisMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(listenerAdapter, topic);

        return container;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(
            RedisConnectionFactory connectionFactory) {

        return new StringRedisTemplate(connectionFactory);
    }
}
