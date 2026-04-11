package org.uwgb.compsci330.client_sdk.cache;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uwgb.compsci330.client_sdk.Client;
import org.uwgb.compsci330.client_sdk.ClientConfig;
import org.uwgb.compsci330.client_sdk.entity.Entity;
import org.uwgb.compsci330.client_sdk.entity.IdentifiableEntity;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

// TODO: Consider adding expiry for items as well to re-fetch latest after certain time.
public class CacheManager implements Entity {
    @Getter
    private final Client client;
    private final ConcurrentHashMap<String, IdentifiableEntity> cache = new ConcurrentHashMap<>();
    private final ConcurrentLinkedQueue<String> insertionOrder = new ConcurrentLinkedQueue<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CacheManager(Client client) {
        this.client = client;
    }

    public <T extends IdentifiableEntity> T getOrCreate(String id, Class<T> type, Supplier<T> factory) {
        return type.cast(cache.computeIfAbsent(id, k -> {
            evictIfNecessary();
            insertionOrder.add(id);

            return factory.get();
        }));
    }

    public <T extends IdentifiableEntity> void put(String id, Class<T> type, T entity) {
        evictIfNecessary();
        insertionOrder.add(id);
        this.cache.put(id, entity);
    }

    public <T extends IdentifiableEntity> Optional<T> get(String id, Class<T> type) {
        return Optional.ofNullable(type.cast(cache.get(id)));
    }

    public boolean invalidate(String id) {
        return this.cache.remove(id) != null;
    }

    private void evictIfNecessary() {
        while (cache.size() >= ClientConfig.MAX_CACHE_SIZE) {
            String oldest = insertionOrder.poll();
            if (oldest != null) {
                logger.info("Evicting element from cache with ID: {}", oldest);
                cache.remove(oldest);
            }
        }
    }
}
