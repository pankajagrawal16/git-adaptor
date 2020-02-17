package com.git.adaptor;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

@RestController
public class DosAndDontsController {
    private final RestTemplate restTemplate;
    private final WebClient webClient;

    @Autowired
    public DosAndDontsController(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(
                options -> options.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                        .compression(true)
                        .afterNettyContextInit(ctx -> {
                            ctx.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                        }));

        this.webClient = WebClient.
                builder()
                .clientConnector(connector)
                .build();
    }

    @GetMapping("/")
    public Mono<String> dontSayHelloLikeThis() {

        return Mono.just(restTemplate.getForObject("http://httpbin.org/delay/1", String.class));
    }

    @GetMapping("/say/hello/do")
    public Mono<String> sayHelloLikeThis() {

        return Mono.fromCallable(() -> restTemplate.getForObject("http://httpbin.org/delay/1", String.class))
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/say/hello/conventional")
    public String conventionalHello() {
        Flux<String> stringFlux = webClient.get().uri("http://httpbin.org/delay/1")
                .retrieve().bodyToFlux(String.class);

        return stringFlux.blockFirst();
    }
}
