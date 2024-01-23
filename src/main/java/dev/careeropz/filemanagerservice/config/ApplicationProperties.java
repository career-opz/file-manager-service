package dev.careeropz.filemanagerservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "storage")
public class ApplicationProperties {
//    private String local;
//    private String s3;
}
