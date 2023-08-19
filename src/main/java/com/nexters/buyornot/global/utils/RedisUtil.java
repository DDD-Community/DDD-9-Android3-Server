package com.nexters.buyornot.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void expire(String key, long seconds) {
        redisTemplate.expire(key, Duration.ofSeconds(seconds));
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public void sAdd(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public void sRem(String key, Object value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    public void getPoll(String key, String field, Long value) {
        redisTemplate.opsForHash().putIfAbsent(key, field, value);
    }

    public boolean alreadyPolled(String key, String userId) {
        if(redisTemplate.opsForHash().hasKey(key, userId)) return true;
        return false;
    }

    public void addPoll(String key, String userId, Long choice) {
        redisTemplate.opsForHash().put(key, userId, choice);
    }

    public List<Long> getPollsByPost(String key) {
        return redisTemplate.opsForHash().values(key)
                .stream()
                .map(c -> Long.parseLong(c.toString()))
                .collect(Collectors.toList());
    }

    //userId, choice mapping
    public Map<Object, Object> getPollStatus(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

}
