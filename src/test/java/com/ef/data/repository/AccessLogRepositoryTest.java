package com.ef.data.repository;

import static org.junit.Assert.assertEquals;

import com.ef.data.model.AccessLog;
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
public class AccessLogRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AccessLogRepository accessLogRepository;

    private final LocalDateTime now = LocalDateTime.now();

    @Test
    public void findLatestAccessDate() {
        List<AccessLog> accessLogList = getAccessLogListOf();
        accessLogList.forEach(accessLog -> testEntityManager.persist(accessLog));
        assertEquals(now.plusDays(4), accessLogRepository.findLatestAccessDate());
    }

    private List<AccessLog> getAccessLogListOf() {
        List<AccessLog> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AccessLog accessLog = new AccessLog();
            accessLog.setAccessDate(now.plusDays((long) i));
            list.add(accessLog);
        }
        return list;
    }
}
