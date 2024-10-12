package ro.iopo.gnir.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class NotificationService {

    private final RedisTemplate<String, String> myRedisTemplate;

    public NotificationService(RedisTemplate<String, String> myRedisTemplate) {
        this.myRedisTemplate = myRedisTemplate;
    }

    public String notifyIfOffline(String chargingStationId) {
        String lockKey = "chargingStationStatus:" + chargingStationId;

        Boolean acquiredLock = myRedisTemplate.opsForValue().setIfAbsent(lockKey, "LOCKED", Duration.ofSeconds(10));

        if (Boolean.TRUE.equals(acquiredLock)) {
            try {
                notifyOtherService(chargingStationId, "UNKNOWN");
                System.out.println("One instance is handling this disconnection for station: " + chargingStationId);
                Thread.sleep(5000L);
                return "One instance handled this disconnection state for station: " + chargingStationId;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                myRedisTemplate.delete(lockKey);
            }
        } else {
            System.out.println("Another instance is already handling this disconnection for station: " + chargingStationId);
            return "Another instance is already handling this disconnection for station: " + chargingStationId;
        }
    }

    private void notifyOtherService(String chargingStationId, String status) {
        System.out.println("Notifying second service: Station " + chargingStationId + " is " + status);
    }
}

