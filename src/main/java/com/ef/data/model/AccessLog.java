package com.ef.data.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
public class AccessLog {

    @Id
    @SequenceGenerator(name = "AccessLogSequence", sequenceName = "ACCESS_LOG_PK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AccessLogSequence")
    private Long id;

    private LocalDateTime accessDate;
    private String ip;
    private String request;
    private Integer status;
    private String userAgent;
}
