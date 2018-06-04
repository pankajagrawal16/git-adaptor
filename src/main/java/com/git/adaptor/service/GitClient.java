package com.git.adaptor.service;

import com.git.adaptor.gitresponse.pullrequest.OpenPR;
import com.git.adaptor.gitresponse.reviews.Review;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

@Component
public class GitClient {

    private final WebClient webClient;

    @Autowired
    public GitClient(GitConfig gitConfig) {
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(
                options -> options.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                        .compression(true)
                        .httpProxy(addressSpec -> addressSpec.host("webguard.posten.no")
                                .port(8080)
                                .nonProxyHosts(null != System.getProperties().getProperty("https.proxyHost") ? null : "api.github.com"))
                        .afterNettyContextInit(ctx -> {
                            ctx.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                        }));

        this.webClient = WebClient.
                builder()
                .clientConnector(connector)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add("Authorization", "token " + gitConfig.getToken());
                    httpHeaders.add("Accepts", "application/vnd.github.v3+json,application/json;q=0.9");
                })
                .build();
    }

    public Flux<OpenPR> openPR(String org, String repo) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/repos/{org}/{repo}/pulls")
                        .scheme("https")
                        .queryParam("state", "open")
                        .host("api.github.com")
                        .build(org, repo)).exchange().
                        flatMapMany(clientResponse -> clientResponse.bodyToFlux(OpenPR.class));
    }

    public Flux<Review> reviewOnPR(String org, String repo, int prNumber) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/repos/{org}/{repo}/pulls/{prNumber}/reviews")
                        .scheme("https")
                        .queryParam("state", "open")
                        .host("api.github.com")
                        .build(org, repo, prNumber)).exchange().
                        flatMapMany(clientResponse -> clientResponse.bodyToFlux(Review.class));

    }
}
