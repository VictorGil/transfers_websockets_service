package net.devaction.kafka.transferswebsocketsservice.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class ConfigValues {

    @JsonProperty("websockets_server_host")
    private String websocketsServerHost;

    @JsonProperty("websockets_server_port")
    private int websocketsServerPort;

    @JsonProperty("websockets_context_path")
    private String websocketsContextPath;

    @JsonProperty("kafka_bootstrap_servers")
    private String kafkaBootstrapServers;

    @JsonProperty("kafka_schema_registry_URL")
    private String kafkaSchemaRegistryUrl;

    @JsonProperty("cadence_domain")
    private String cadenceDomain;

    @Override
    public String toString() {
        return "ConfigValues [websocketsServerHost=" + websocketsServerHost + ", websocketsServerPort=" + websocketsServerPort
                + ", websocketsContextPath=" + websocketsContextPath + ", kafkaBootstrapServers=" + kafkaBootstrapServers
                + ", kafkaSchemaRegistryUrl=" + kafkaSchemaRegistryUrl + ", cadenceDomain=" + cadenceDomain + "]";
    }

    public String getWebsocketsServerHost() {
        return websocketsServerHost;
    }

    public void setWebsocketsServerHost(String websocketsServerHost) {
        this.websocketsServerHost = websocketsServerHost;
    }

    public int getWebsocketsServerPort() {
        return websocketsServerPort;
    }

    public void setWebsocketsServerPort(int websocketsServerPort) {
        this.websocketsServerPort = websocketsServerPort;
    }

    public String getWebsocketsContextPath() {
        return websocketsContextPath;
    }

    public void setWebsocketsContextPath(String websocketsContextPath) {
        this.websocketsContextPath = websocketsContextPath;
    }

    public String getKafkaBootstrapServers() {
        return kafkaBootstrapServers;
    }

    public void setKafkaBootstrapServers(String kafkaBootstrapServers) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
    }

    public String getKafkaSchemaRegistryUrl() {
        return kafkaSchemaRegistryUrl;
    }

    public void setKafkaSchemaRegistryUrl(String kafkaSchemaRegistryUrl) {
        this.kafkaSchemaRegistryUrl = kafkaSchemaRegistryUrl;
    }

    public String getCadenceDomain() {
        return cadenceDomain;
    }

    public void setCadenceDomain(String cadenceDomain) {
        this.cadenceDomain = cadenceDomain;
    }
}
