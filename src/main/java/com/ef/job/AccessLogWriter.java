package com.ef.job;

import com.ef.data.model.AccessLog;
import com.ef.data.repository.AccessLogRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class AccessLogWriter implements ItemWriter<AccessLog> {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Override
    public void write(final List<? extends AccessLog> list) {
        log.info("Writing {} items...", list.size());
        accessLogRepository.saveAll(list);
    }
}
