package com.git.adaptor;

import com.git.adaptor.response.ErrorResponse;
import com.git.adaptor.response.PullRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@WebFluxTest
public class PullRequestHandlerJavaTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void get_open_pr() {
        webClient.get().uri("/git/pullrequest/{org}/{repo}", "pankajagrawal16", "sonar-voilation-assign")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PullRequest.class);
    }

    @Test
    public void get_open_pr_bad_request() {
        webClient.get().uri("/git/pullrequest/{org}/{repo}", "pankajagrawal16", "sonar-voilation-assigns")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ErrorResponse.class)
                .isEqualTo(new ErrorResponse("NOT FOUND", "No Open PR found for repo. Check if repo name is correct."));
    }
}