package com.git.adaptor;

import com.git.adaptor.response.ErrorResponse;
import com.git.adaptor.response.PullRequest;
import com.git.adaptor.service.GitClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

public class PullRequestHandlerJava implements HandlerFunction<ServerResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(PullRequestHandlerJava.class);
    private final GitClient gitClient;
    private AuditLogRepo auditLogRepo;

    public PullRequestHandlerJava(GitClient gitClient,
                                  AuditLogRepo auditLogRepo) {
        this.gitClient = gitClient;
        this.auditLogRepo = auditLogRepo;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        String repo = request.pathVariable("repo");
        String org = request.pathVariable("org");

        return gitClient.openPR(org, repo)
                .filter(openPR -> null != openPR.url)
                .flatMap(openPR -> gitClient.reviewOnPR(org, repo, openPR.number)
                        .reduce((review, review2) -> {
                            if (review.getSubmittedAtAsDate().isBefore(review2.getSubmittedAtAsDate())) {
                                return review2;
                            }

                            return review;
                        })
                        .flux()
                        .map(review -> new PullRequest(openPR.url, openPR.user.login, openPR.createdAt, review.user.login, review.state))
                        .defaultIfEmpty(new PullRequest(openPR.url, openPR.user.login, openPR.createdAt, "NO ONE", "NO ACTION")))
                .collectList()
                .doOnNext(pullRequest -> auditLogRepo.log(new AuditLog(new Random().nextInt(), org, repo, pullRequest.size(), ZonedDateTime.now())))
                .flatMap((Function<List<PullRequest>, Mono<ServerResponse>>) pullRequests -> {

                    if (pullRequests.isEmpty()) {
                        return badRequest().body(BodyInserters.fromObject(new ErrorResponse("NOT FOUND", "No Open PR found for repo. Check if repo name is correct.")));
                    }

                    return ok().body(BodyInserters.fromObject(pullRequests));
                })
                .doOnError(throwable -> LOG.error("Failed with error {}", throwable.getMessage(), throwable))
                .onErrorResume(throwable -> status(HttpStatus.INTERNAL_SERVER_ERROR).body(BodyInserters.fromObject(new ErrorResponse("INTERNAL_SERVER_ERROR", "Server error has occurred!"))));
    }
}
