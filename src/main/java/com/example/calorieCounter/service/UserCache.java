package com.example.calorieCounter.service;

import com.example.calorieCounter.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserCache {

    private final Map<Long, UserDTO> cache = new ConcurrentHashMap<>();

    public void put(UserDTO user) {
        cache.put(user.getUserId(), user);
    }

    public UserDTO get(Long id) {
        return cache.get(id);
    }
}
