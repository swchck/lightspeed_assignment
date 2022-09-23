package com.lightspeed.lightspeed_assignment.handler;

import com.lightspeed.lightspeed_assignment.entity.Counter;
import com.lightspeed.lightspeed_assignment.util.Database;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CounterHandler {
    private final Database database;

    public Mono<ServerResponse> getCounters(ServerRequest request) {
        return database.getAllRecords()
                .flatMap(records -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(records)
                );
    }

    public Mono<ServerResponse> getCounterByName(ServerRequest request) {
        final String name = request.pathVariable("name");
        return database.getByName(name)
                .flatMap(counter -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(counter)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createCounter(ServerRequest request) {
        Mono<Counter> counterWrapper = request.bodyToMono(Counter.class);
        return counterWrapper
                .flatMap(counter -> database.create(counter.getName(), counter.getValue())
                        .flatMap(response -> ServerResponse.created(request.uri()).build())
                        .switchIfEmpty(ServerResponse.created(request.uri()).build())
                        .onErrorResume(err -> Mono.just("Error " + err.getMessage())
                                .flatMap(s -> ServerResponse.status(HttpStatus.NOT_MODIFIED)
                                        .contentType(MediaType.TEXT_PLAIN)
                                        .bodyValue(s)))
                );
    }

    public Mono<ServerResponse> incrementCounter(ServerRequest request) {
        final String counterName = request.pathVariable("name");
        return database.incrementByName(counterName)
                .flatMap(counter -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(counter)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteCounter(ServerRequest request) {
        final String counterName = request.pathVariable("name");
        return database.delete(counterName)
                .flatMap(counter -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(counter)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getAllCounterSum(ServerRequest request) {
        return database.getAllCountersSum()
                .flatMap(result -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue("All counters sum: " + result))
                );
    }

}
