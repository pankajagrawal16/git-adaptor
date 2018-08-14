package com.git.adaptor

import com.git.adaptor.service.GitClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@SpringBootApplication
@Configuration
@ComponentScan("com.git")
open class GitAdaptorApplication {

    @Bean
    @Autowired
    internal open fun route(gitClient: GitClient,
        auditLogRepo: AuditLogRepo): RouterFunction<*> {
        return RouterFunctions
            .route<ServerResponse>(GET("/git/pullrequest/{org}/{repo}"),
                PullRequestHandlerJava(gitClient, auditLogRepo))
            .andRoute(GET("/git/pullrequest/{org}/{repo}/latest"),
                LatestPullRequestHandlerJava(gitClient, auditLogRepo))
            .andRoute(GET("/git/audit/log/{org}/{repo}"),
                AuditLogHandler(auditLogRepo))

    }

    @Bean
    open fun restTemplate() : RestTemplate {
        return RestTemplate()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(GitAdaptorApplication::class.java)
        }
    }
}
