package com.example.EmployeManagement.Redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeePublisher {

    private final StringRedisTemplate redisTemplate;

    private final ChannelTopic topic;

    public void publishEmployeeCreated(Integer employeeId) {

        redisTemplate.convertAndSend(
                topic.getTopic(),
                String.valueOf(employeeId)
        );
    }

}