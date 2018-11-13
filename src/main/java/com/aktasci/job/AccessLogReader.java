package com.aktasci.job;

import com.aktasci.data.model.AccessLog;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

class AccessLogReader extends FlatFileItemReader<AccessLog> {

    private static final String DELIMITER = "|";
    private static final String READER_NAME = "AccessLogReader";
    private static final boolean TOKENS_STRICT = false;
    private static final String FIELD_ACCESS_DATE = "accessDate";
    private static final String FIELD_IP = "ip";
    private static final String FIELD_REQUEST = "request";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_USER_AGENT = "userAgent";
    private static final String DATE_TIME_FORMAT = "yyy-MM-dd HH:mm:ss.SSS";

    public AccessLogReader(String filePath) {
        this.setResource(new FileSystemResource(filePath));
        this.setName(READER_NAME);
        this.setLineMapper(getLineMapper());
    }

    private LineMapper<AccessLog> getLineMapper() {
        DefaultLineMapper<AccessLog> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(DELIMITER);
        lineTokenizer.setStrict(TOKENS_STRICT);
        lineTokenizer.setNames(getNames());

        FieldSetMapper<AccessLog> fieldSetMapper = getFieldSetMapper();

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

    private String[] getNames() {
        return new String[]{FIELD_ACCESS_DATE, FIELD_IP, FIELD_REQUEST, FIELD_STATUS, FIELD_USER_AGENT};
    }

    private FieldSetMapper<AccessLog> getFieldSetMapper() {
        return fieldSet -> {
            AccessLog accessLog = new AccessLog();
            accessLog.setAccessDate(LocalDateTime
                    .parse(fieldSet.readString(FIELD_ACCESS_DATE), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
            accessLog.setIp(fieldSet.readString(FIELD_IP));
            accessLog.setRequest(fieldSet.readString(FIELD_REQUEST));
            accessLog.setStatus(fieldSet.readInt(FIELD_STATUS));
            accessLog.setUserAgent(fieldSet.readString(FIELD_USER_AGENT));
            return accessLog;
        };
    }
}
