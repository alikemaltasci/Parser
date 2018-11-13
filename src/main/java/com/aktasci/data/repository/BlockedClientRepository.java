package com.aktasci.data.repository;

import com.aktasci.data.model.BlockedClient;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BlockedClientRepository extends CrudRepository<BlockedClient, Long> {

    @Query("SELECT new BlockedClient(t.ip,COUNT(t.ip)) FROM AccessLog t "
            + "WHERE t.accessDate between :startDate and :endDate GROUP BY t.ip HAVING COUNT(t.ip) >= :threshold")
    List<BlockedClient> findClientsToBeBlocked(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate, @Param("threshold") Long threshold);
}
