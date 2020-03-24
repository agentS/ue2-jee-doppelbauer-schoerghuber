package eu.nighttrains.configuration.redis;

import org.eclipse.microprofile.config.spi.ConfigSource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RedisConfigurationSource implements ConfigSource {
    private static final int DEFAULT_CONFIGURATION_ORDINAL = 600;

    private static final String REDIS_HOSTNAME = "127.0.0.1";
    private static final int REDIS_PORT = 6379;
    private static final int REDIS_POOL_OPENING_TIMEOUT = 3000;
    private static final int REDIS_POOL_MAXIMUM_SIZE = 5;

    private static final String PREFIX = "timetable";
    private static final String KEY_SEPARATOR = ":";
    private static final String ORDINAL_KEY = "ordinal";

    private JedisPool redisPool;

    public RedisConfigurationSource() {
        JedisPoolConfig configuration = new JedisPoolConfig();
        configuration.setTestOnBorrow(true);
        configuration.setMaxTotal(REDIS_POOL_MAXIMUM_SIZE);
        this.redisPool = new JedisPool(
                configuration,
                REDIS_HOSTNAME, REDIS_PORT,
                REDIS_POOL_OPENING_TIMEOUT
        );
    }

    @Override
    public Map<String, String> getProperties() {
        try (Jedis redisClient = this.redisPool.getResource()) {
            Map<String, String> properties = new HashMap<>();
            Set<String> keys = redisClient.hkeys(PREFIX);
            for (var key : keys) {
                properties.put(key, redisClient.hget(PREFIX, key));
            }
            return properties;
        }
    }

    @Override
    public Set<String> getPropertyNames() {
        try (Jedis redisClient = this.redisPool.getResource()) {
            return redisClient.hkeys(PREFIX);
        }
    }

    @Override
    public String getValue(String path) {
        try (Jedis redisClient = this.redisPool.getResource()) {
            return redisClient.hget(PREFIX, path);
        }
    }

    @Override
    public String getName() {
        return RedisConfigurationSource.class.getSimpleName();
    }

    @Override
    public int getOrdinal() {
        try (Jedis redisClient = this.redisPool.getResource()) {
            return Integer.parseInt(redisClient.get(PREFIX + KEY_SEPARATOR + ORDINAL_KEY));
        }
    }

    @PreDestroy
    public void close() {
        if (this.redisPool != null) {
            this.redisPool.close();
        }
    }
}
