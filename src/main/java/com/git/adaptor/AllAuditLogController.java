package com.git.adaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Optional;

@RestController
@RequestMapping("/git/audit/latest/")
public class AllAuditLogController {

    private final AuditLogRepo auditLogRepo;

    @Autowired
    public AllAuditLogController(AuditLogRepo auditLogRepo) {
        this.auditLogRepo = auditLogRepo;
    }

    @GetMapping(value = {"/log", "/log/{count}"})
    public Flux<AuditLog> allLogs(@PathVariable Optional<Integer> count) {
        Integer numberOfLogEntries = count.orElse(10);
        return auditLogRepo.queryAll(numberOfLogEntries);
    }
}
