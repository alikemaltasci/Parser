package com.aktasci.data.model;

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
  @SequenceGenerator(name = "ACCESS_LOG_SEQ")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCESS_LOG_SEQ")
  private Long id;

  private LocalDateTime accessDate;
  private String ip;
  private String request;
  private Integer status;
  private String userAgent;
}
