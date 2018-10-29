package com.ef.data.repository;

import static org.junit.Assert.assertEquals;

import com.ef.data.model.AccessLog;
import com.ef.data.model.BlockedClient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BlockedClientRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BlockedClientRepository blockedClientRepository;
    private final LocalDateTime now = LocalDateTime.now();

    @Test
    public void findClientsToBeBlocked() {
        List<AccessLog> accessLogList = getAccessLogListOf();
        accessLogList.forEach(accessLog -> testEntityManager.persist(accessLog));
        List<BlockedClient> clientsToBeBlocked = blockedClientRepository
                .findClientsToBeBlocked(now, now.plusHours(1L), 1L);
        assertEquals(7, clientsToBeBlocked.size());
    }

    private List<AccessLog> getAccessLogListOf() {
        List<AccessLog> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AccessLog accessLog = new AccessLog();
            accessLog.setIp("IP_" + i);
            accessLog.setAccessDate(now.plusMinutes(i * 10));
            list.add(accessLog);
        }

        return list;
    }
}
