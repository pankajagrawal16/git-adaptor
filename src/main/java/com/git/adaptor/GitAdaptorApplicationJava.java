package com.git.adaptor;

import com.git.adaptor.service.GitClient;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/*@SpringBootApplication
@EnableConfigurationProperties*/
public class GitAdaptorApplicationJava {
    public static void main(String[] args) {
        SpringApplication.run(GitAdaptorApplicationJava.class);
    }

    @Bean
    RouterFunction<?> route(GitClient gitClient,
                            AuditLogRepo auditLogRepo) {
        return RouterFunctions
                .route(RequestPredicates.GET("/git/pullrequest/{org}/{repo}"), new PullRequestHandlerJava(gitClient, auditLogRepo));
    }
}