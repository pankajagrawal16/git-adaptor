package com.git.adaptor.service;

import com.git.adaptor.gitresponse.pullrequest.OpenPR;
import com.git.adaptor.gitresponse.reviews.Review;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebFluxTest
@Ignore
public class GitClientTest {

    @Autowired
    private GitClient gitClient;

    @Test
    public void open_pr() {
        Flux<OpenPR> fraktguide = gitClient.openPR("pankajagrawal16", "sonar-voilation-assign");

        StepVerifier.create(fraktguide)
                .consumeNextWith(openPR -> assertEquals("https://api.github.com/repos/pankajagrawal16/sonar-voilation-assign/pulls/1", openPR.url))
                .thenCancel()
                .verify();
    }

    @Test
    public void review_on_pr() {
        Flux<Review> review = gitClient.reviewOnPR("pankajagrawal16", "sonar-voilation-assign", 1);

        StepVerifier.create(review)
                .expectComplete()
                .verify();
    }
}