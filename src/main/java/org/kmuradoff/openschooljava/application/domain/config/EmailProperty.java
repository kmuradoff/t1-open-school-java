package org.kmuradoff.openschooljava.application.domain.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("t1.email")
public class EmailProperty {

    /**
     * Email sender username
     */
    private String from;

    /**
     * Email receiver username
     */
    private String to;

    /**
     * Email subject
     */
    private String subject;
}
