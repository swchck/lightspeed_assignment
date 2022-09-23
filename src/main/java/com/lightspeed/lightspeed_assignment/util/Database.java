package com.lightspeed.lightspeed_assignment.util;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Database {
    public record Counter(String name, AtomicInteger value) {
    }

    private final ConcurrentHashMap<String, Counter> database = new ConcurrentHashMap<>();

    @PostConstruct
    private void setUp() {
        database.put("name", new Counter("name", new AtomicInteger(1)));
        database.put("hello", new Counter("hello", new AtomicInteger(2)));
    }

    public Mono<ConcurrentHashMap<String, Counter>> getAllRecords() {
        return Mono.defer(() -> Mono.just(database));
    }

    public Mono<Counter> getByName(String name) {
        return Mono.defer(() -> Mono.justOrEmpty(database.get(name)));
    }

    public Mono<Counter> create(String name, Integer value) {
        if (database.containsKey(name)) {
            return Mono.error(new Exception("Counter with this name already in list"));
        }
        return Mono.justOrEmpty(database.put(name, new Counter(name, new AtomicInteger(value))));
    }

    public Mono<Counter> delete(String name) {
        return Mono.defer(() -> Mono.justOrEmpty(database.remove(name)));
    }

    public Mono<Integer> incrementByName(String name) {
        return Mono.defer(() -> Mono.justOrEmpty(database.get(name))
                .map(result -> result.value.incrementAndGet())
                .switchIfEmpty(Mono.empty())
        );
    }

    public Mono<Integer> getAllCountersSum() {
        return Mono.defer(() -> Mono.just(
                database.values().stream()
                        .map(Counter::value)
                        .map(AtomicInteger::get)
                        .reduce(0, Integer::sum)
        ));
    }
}
