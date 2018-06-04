package com.git.adaptor

import com.git.adaptor.response.ErrorResponse
import com.git.adaptor.response.PullRequest
import com.git.adaptor.service.GitClient
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class PullRequestHandler(private val gitClient: GitClient) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val repo = request.pathVariable("repo")
        val org = request.pathVariable("org")

        return gitClient.openPR(org, repo)
            .filter { t -> null != t?.number }
            .flatMap { openPR ->
                val reviewOnPR = gitClient.reviewOnPR(org, repo, openPR.number)

                reviewOnPR
                    .reduce { review, review2 ->
                        when {
                            review.submittedAtAsDate.isBefore(review2.submittedAtAsDate) -> review2
                            else -> review
                        }
                    }
                    .flux()
                    .map { review ->
                        PullRequest(openPR.url, openPR.user.login, openPR.createdAt, review.user.login, review.state)
                    }.defaultIfEmpty(PullRequest(openPR.url, openPR.user.login, openPR.createdAt))
            }.collectList()
            .flatMap { prs ->
                if (prs.isEmpty()) {
                    ServerResponse.badRequest().body(BodyInserters.fromObject(ErrorResponse("NOT FOUND", "No Open PR found for repor. Check if repo name is correct.")))
                } else {
                    ServerResponse.ok().body(BodyInserters.fromObject(prs))
                }
            }
    }
}
