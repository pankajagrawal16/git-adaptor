package com.git.adaptor;

import com.github.davidmoten.rx.jdbc.ConnectionProviderFromUrl;
import com.github.davidmoten.rx.jdbc.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;
import java.util.List;

import static java.time.ZoneId.systemDefault;
import static java.time.ZonedDateTime.ofInstant;

@Repository
public class AuditLogRepo {
    private static final Logger LOG = LoggerFactory.getLogger(AuditLogRepo.class);
    private final Database db;

    @Autowired
    public AuditLogRepo(DatabaseConfig databaseConfig) {
        db = Database.from(new ConnectionProviderFromUrl(databaseConfig.getUrl(), databaseConfig.getUsername(), databaseConfig.getPassword()));

    }

    void log(AuditLog auditLog) {
        LOG.info("Saving log {}", auditLog);

        int rows = db.update("insert into audit_log(id, org, application, openpr, queryat) values (?, ?, ?, ?, ?)")
                .parameters(auditLog.getId(), auditLog.getOrg(), auditLog.getApplication(), auditLog.getOpenPrCount(), Timestamp.from(auditLog.getQueryAt().toInstant()))
                .execute();

        LOG.info("Saved rows {}", rows);
    }


    List<AuditLog> query(String org,
                         String application) {
        return db.select("select * from audit_log where org=? and application=?")
                .parameters(org, application)
                .get(rs -> new AuditLog(rs.getInt("id"),
                        rs.getString("org"),
                        rs.getString("application"),
                        rs.getInt("openpr"),
                        ofInstant(rs.getTimestamp("queryat").toInstant(), systemDefault())))
                .toList()
                .toBlocking()
                .first();
    }

    Flux<AuditLog> queryAll(int limit) {
        return Flux.fromStream(db.select("select * from audit_log order by queryat desc LIMIT ?")
                .parameters(limit)
                .get(rs -> new AuditLog(rs.getInt("id"),
                        rs.getString("org"),
                        rs.getString("application"),
                        rs.getInt("openpr"),
                        ofInstant(rs.getTimestamp("queryat").toInstant(), systemDefault())))
                .toList()
                .toBlocking()
                .first().stream());

    }
}
