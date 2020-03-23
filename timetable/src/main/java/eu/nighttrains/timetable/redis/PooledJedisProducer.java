package eu.nighttrains.timetable.redis;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
public class PooledJedisProducer {
    private JedisPool pool = null;

    @ConfigProperty(name = "configuration.redis.hostname")
    private String hostname;

    @ConfigProperty(name = "configuration.redis.port")
    private int port;

    @ConfigProperty(name = "configuration.redis.username", defaultValue = "")
    private String username;

    @ConfigProperty(name = "configuration.redis.password", defaultValue = "")
    private String password;

    @ConfigProperty(name = "configuration.redis.poolSize", defaultValue = "5")
    private int poolSize;

    private static final int POOL_ESTABLISHMENT_TIMEOUT = 3000;

    @PostConstruct
    public void initializePool() {
        JedisPoolConfig configuration = new JedisPoolConfig();
        configuration.setTestOnBorrow(true);
        configuration.setMaxTotal(this.poolSize);
        this.pool = new JedisPool(
                configuration,
                this.hostname, this.port,
                POOL_ESTABLISHMENT_TIMEOUT
        );
    }

    @Produces
    public Jedis getPooledClient(InjectionPoint injectionPoint) {
        return this.pool.getResource();
    }

    public void releaseRedisClientToPool(@Disposes Jedis client) {
        client.close();
    }

    @PreDestroy
    public void close() {
        if (this.pool != null) {
            this.pool.close();
        }
    }
}
