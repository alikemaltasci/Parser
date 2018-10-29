package com.ef.job;

import com.ef.data.model.AccessLog;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.batch.item.ItemProcessor;

class DateFilterProcessor implements ItemProcessor<AccessLog, AccessLog> {

    private final LocalDateTime latestAccessDate;

    public DateFilterProcessor(final LocalDateTime latestAccessDateArg) {
        this.latestAccessDate = latestAccessDateArg;
    }

    @Override
    public AccessLog process(final AccessLog accessLog) {
        if (isDateAfter(accessLog)) {
            return accessLog;
        }
        return null;
    }

    private boolean isDateAfter(final AccessLog accessLog) {
        LocalDateTime accessDateInSec = accessLog.getAccessDate().truncatedTo(ChronoUnit.SECONDS);
        return accessDateInSec.isAfter(latestAccessDate);
    }
}
