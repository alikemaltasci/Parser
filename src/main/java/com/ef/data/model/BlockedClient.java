package com.ef.data.model;

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
    @SequenceGenerator(name = "BlockedClientSequence", sequenceName = "BLOCKED_CLIENT_PK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BlockedClientSequence")
    private Long id;

    private String ip;
    private Long requestCount;
    private String description;

    public BlockedClient(final String ipArg, final Long requestCountArg) {
        this.ip = ipArg;
        this.requestCount = requestCountArg;
    }
}
