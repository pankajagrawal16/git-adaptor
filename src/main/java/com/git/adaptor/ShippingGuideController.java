package com.git.adaptor;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/shippingguide/products")
public class ShippingGuideController {

    @GetMapping(value = "/prices", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> prices(@RequestParam("product") List<String> products) {
       return Flux.fromStream(products.stream())
                .map(Product::new)
                .delayElements(Duration.ofMillis(500));
    }
}
