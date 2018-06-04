package com.git.adaptor;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ParallelFlux;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/shippingguide/products")
public class ShippingGuideController {

    private final WebClient webClient;

    @Autowired
    public ShippingGuideController(ShippingGuideConfig shippingGuideConfig) {
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(
                options -> options.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                        .compression(true)
                        .afterNettyContextInit(ctx -> {
                            ctx.addHandlerLast(new ReadTimeoutHandler(500000, TimeUnit.SECONDS));
                        }));

        this.webClient = WebClient.
                builder()
                .clientConnector(connector)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add("X-MyBring-API-Uid", shippingGuideConfig.getUsername());
                    httpHeaders.add("X-MyBring-API-Key", shippingGuideConfig.getApikey());
                    httpHeaders.add("Accept", "application/json");
                })
                .build();
    }

   /* @GetMapping(value = "/prices", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> pricesFlux(@RequestParam("product") List<String> products) {
        return Flux.fromStream(products.stream())
                .delayElements(Duration.ofSeconds(1))
                .flatMap(this::callSg);
    }*/

    @GetMapping(value = "/prices", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ParallelFlux<Product> prices(@RequestParam("product") List<String> products) {
        return Flux.fromStream(products.stream())
                .delayElements(Duration.ofSeconds(1))
                .parallel()
                .flatMap(this::callSgParallel);
    }

    @NotNull
    private Flux<Product> callSg(String product) {
        return webClient.get()
                .uri("https://api.bring.com/shippingguide/v2/products?fromcountry=NO&tocountry=NO&frompostalcode=0010&topostalcode=9990&product={product}&weight=10", product)
                .exchange()
                .flatMapMany(crs -> crs.bodyToFlux(String.class)
                        .map(s -> {
                            try {
                                JsonNode jsonNode = new ObjectMapper().readTree(s);
                                return new Product(jsonNode.withArray("consignments").get(0).withArray("products").get(0).get("productionCode").asText());
                            } catch (IOException e) {
                                throw new RuntimeException();
                            }
                        }));
    }

    @NotNull
    private ParallelFlux<Product> callSgParallel(String product) {
        return webClient.get()
                .uri("https://api.bring.com/shippingguide/v2/products?fromcountry=NO&tocountry=NO&frompostalcode=0010&topostalcode=9990&product={product}&weight=10", product)
                .exchange()
                .flatMapMany(crs -> crs.bodyToFlux(String.class)
                        .map(s -> {
                            try {
                                JsonNode jsonNode = new ObjectMapper().readTree(s);
                                return new Product(jsonNode.withArray("consignments").get(0).withArray("products").get(0).get("productionCode").asText());
                            } catch (IOException e) {
                                throw new RuntimeException();
                            }
                        }))
                .parallel();
    }
}
