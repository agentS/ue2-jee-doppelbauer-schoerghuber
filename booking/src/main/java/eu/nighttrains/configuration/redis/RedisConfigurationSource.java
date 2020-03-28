package eu.nighttrains.configuration.redis;

import org.eclipse.microprofile.config.spi.ConfigSource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PreDestroy;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RedisConfigurationSource implements ConfigSource {
    private static final int DEFAULT_CONFIGURATION_ORDINAL = 600;

    private static final String CONFIGURATION_FILE_NAME = "/redisConfiguration.json";

    private static final String KEY_SEPARATOR = ":";
    private static final String ORDINAL_KEY = "ordinal";

    private JedisPool redisPool;
    private final String prefix;

    public RedisConfigurationSource() throws IOException {
        try (FileReader configurationFileReader = new FileReader(this.getClass().getResource(CONFIGURATION_FILE_NAME).getFile())) {
            try (JsonReader jsonReader = Json.createReader(configurationFileReader)) {
                JsonObject jsonConfiguration = jsonReader.readObject();
                JedisPoolConfig configuration = new JedisPoolConfig();
                configuration.setTestOnBorrow(true);
                configuration.setMaxTotal(jsonConfiguration.getInt("maximumPoolSize"));
                this.redisPool = new JedisPool(
                        configuration,
                        jsonConfiguration.getString("hostname"), jsonConfiguration.getInt("port"),
                        jsonConfiguration.getInt("timeout")
                );
                this.prefix = jsonConfiguration.getString("prefix");
            }
        }
    }

    @Override
    public Map<String, String> getProperties() {
        try (Jedis redisClient = this.redisPool.getResource()) {
            Map<String, String> properties = new HashMap<>();
            Set<String> keys = redisClient.hkeys(this.prefix);
            for (var key : keys) {
                properties.put(key, redisClient.hget(this.prefix, key));
            }
            return properties;
        }
    }

    @Override
    public Set<String> getPropertyNames() {
        try (Jedis redisClient = this.redisPool.getResource()) {
            return redisClient.hkeys(this.prefix);
        }
    }

    @Override
    public String getValue(String path) {
        try (Jedis redisClient = this.redisPool.getResource()) {
            return redisClient.hget(this.prefix, path);
        }
    }

    @Override
    public String getName() {
        return RedisConfigurationSource.class.getSimpleName();
    }

    @Override
    public int getOrdinal() {
        try (Jedis redisClient = this.redisPool.getResource()) {
            return Integer.parseInt(redisClient.get(this.prefix + KEY_SEPARATOR + ORDINAL_KEY));
        }
    }

    @PreDestroy
    public void close() {
        if (this.redisPool != null) {
            this.redisPool.close();
        }
    }
}
