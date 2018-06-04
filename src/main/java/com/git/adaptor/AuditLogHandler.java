package com.git.adaptor;

import com.git.adaptor.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public class AuditLogHandler implements HandlerFunction<ServerResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(AuditLogHandler.class);

    private final AuditLogRepo auditLogRepo;

    public AuditLogHandler(AuditLogRepo auditLogRepo) {
        this.auditLogRepo = auditLogRepo;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        String repo = request.pathVariable("repo");
        String org = request.pathVariable("org");

        return Flux.fromStream(auditLogRepo.query(org, repo).stream())
                .collectList()
                .flatMap((Function<List<AuditLog>, Mono<ServerResponse>>) pullRequests -> {

                    if (pullRequests.isEmpty()) {
                        return badRequest().body(BodyInserters.fromObject(new ErrorResponse("NOT FOUND", "No Open PR found for repo. Check if repo name is correct.")));
                    }

                    return ok().body(BodyInserters.fromObject(pullRequests));
                });
    }
}
