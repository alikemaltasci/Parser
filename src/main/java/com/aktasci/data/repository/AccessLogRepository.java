package com.aktasci.data.repository;

import com.aktasci.data.model.AccessLog;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends CrudRepository<AccessLog, Long> {

    @Query("SELECT MAX(t.accessDate) FROM AccessLog t")
    LocalDateTime findLatestAccessDate();
}
