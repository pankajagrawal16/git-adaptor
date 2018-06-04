package com.git.adaptor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RunWith(SpringRunner.class)
@WebFluxTest(AllAuditLogController.class)
public class AllAuditLogControllerTest {
    @Autowired
    private WebTestClient webClient;

    @Test
    public void get_one_audit_log() {
        webClient.get().uri("/git/audit/latest/log/{count}", 1)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(AuditLog.class)
                .hasSize(1);
    }

    @Test
    public void get_default_audit_log() {
        webClient.get().uri("/git/audit/latest/log")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(AuditLog.class)
                .hasSize(10);
    }
}