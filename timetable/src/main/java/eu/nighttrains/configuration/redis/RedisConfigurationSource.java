package eu.nighttrains.configuration.redis;

import org.eclipse.microprofile.config.spi.ConfigSource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Set;

public class RedisConfigurationSource implements ConfigSource {
    private static final int CONFIGURATION_ORDINAL = 600;

    private static final String REDIS_HOSTNAME = "127.0.0.1";
    private static final int REDIS_PORT = 6379;
    private static final int REDIS_POOL_OPENING_TIMEOUT = 3000;
    private static final int REDIS_POOL_MAXIMUM_SIZE = 5;

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
        throw new RuntimeException("redis pool: " + this.redisPool);
    }

    @Override
    public Map<String, String> getProperties() {
        return null;
    }

    @Override
    public Set<String> getPropertyNames() {
        return null;
    }

    @Override
    public String getValue(String path) {
        return null;
    }

    @Override
    public String getName() {
        return RedisConfigurationSource.class.getSimpleName();
    }

    @Override
    public int getOrdinal() {
        return CONFIGURATION_ORDINAL;
    }

    @PreDestroy
    public void close() {
        if (this.redisPool != null) {
            this.redisPool.close();
        }
    }
}
