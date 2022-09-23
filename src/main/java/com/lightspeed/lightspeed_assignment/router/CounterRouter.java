package com.lightspeed.lightspeed_assignment.router;

import com.lightspeed.lightspeed_assignment.handler.CounterHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Component
public class CounterRouter {

    @Bean
    public RouterFunction<ServerResponse> route(CounterHandler counterHandler) {
        final String BASE_PATH = "/api/v1";
        return RouterFunctions
                .route(GET(BASE_PATH + "/counters").and(accept(MediaType.APPLICATION_JSON)),
                        counterHandler::getCounters)
                .andRoute(GET(BASE_PATH + "/counters/{name}").and(accept(MediaType.APPLICATION_JSON)),
                        counterHandler::getCounterByName)
                .andRoute(POST(BASE_PATH).and(accept(MediaType.APPLICATION_JSON)),
                        counterHandler::createCounter)
                .andRoute(POST(BASE_PATH + "/counters/{name}").and(accept(MediaType.APPLICATION_JSON)),
                        counterHandler::incrementCounter)
                .andRoute(DELETE(BASE_PATH + "/counters/{name}").and(accept(MediaType.APPLICATION_JSON)),
                        counterHandler::deleteCounter)
                .andRoute(GET(BASE_PATH + "/sum").and(accept(MediaType.APPLICATION_JSON)),
                        counterHandler::getAllCounterSum);
    }
}
