package edu.miu.common.config.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "common")
public class CommonProperties {
    @NestedConfigurationProperty
    private ServerProperties server = new ServerProperties();

    @NestedConfigurationProperty
    private MvcProperties mvc = new MvcProperties();

    @NestedConfigurationProperty
    private SecurityProperties security = new SecurityProperties();

    @NestedConfigurationProperty
    private FeignOAuthProperties feignOauth = new FeignOAuthProperties();

    @NestedConfigurationProperty
    private SyncProperties sync = new SyncProperties();

    @Data
    public static class ServerProperties {
        private Integer httpPort;
    }

    @Data
    public static class MvcProperties {
        private boolean enabled;
    }

    @Data
    public static class SecurityProperties {
        private boolean enabled;

        @NestedConfigurationProperty
        private EndPointProperties api = new EndPointProperties(false, "/v1/api");

        @NestedConfigurationProperty
        private EndPointProperties hmi = new EndPointProperties(false, "/v1/hmi");

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class EndPointProperties {
            private boolean enabled;

            private String pathPrefix = "/v1/api";
        }
    }

    @Data
    public static class FeignOAuthProperties {
        private boolean enabled;
    }

    @Data
    public static class SyncProperties {
        private boolean bootstrap;

        private boolean masterSwitchEnabled;

        @NestedConfigurationProperty
        private ScheduleProperties schedule = new ScheduleProperties();

        @Data
        public static class ScheduleProperties {
            private String outageStart = "0 30 22 * * ?";

            private String outageEnd = "0 29 02 * * ?";

            private String monthly = "0 30 04 27 * ?";

            private String weekly = "0 30 03 * * MON";

            private String daily = "0 30 02 * * ?";

            private String twiceDaily = "0 30 6,18 * * ?";

            private String hexDaily = "0 30 5/4 * * ?";

            private String hourly = "0 45 * * * ?";

            private String fifteenMinutely = "0 9/15 * * * ?";

            private String minutely = "30 * * * * ?";

            private String hourlyOfficeHours = "0 0 9-21 * * ?";

        }
    }
}
