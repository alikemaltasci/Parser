package com.aktasci.data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
public class BlockedClient {

  @Id
  @SequenceGenerator(name = "BLOCKED_CLIENT_SEQ")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BLOCKED_CLIENT_SEQ")
  private Long id;

  private String ip;
  private Long requestCount;
  private String description;

  public BlockedClient(final String ipArg, final Long requestCountArg) {
    this.ip = ipArg;
    this.requestCount = requestCountArg;
  }
}
