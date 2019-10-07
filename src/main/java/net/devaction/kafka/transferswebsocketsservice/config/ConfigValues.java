package net.devaction.kafka.transferswebsocketsservice.config;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class ConfigValues {

    @JsonProperty("server_host")
    private String serverHost;

    @JsonProperty("server_port")
    private int serverPort;

    @JsonProperty("context_path")
    private String contextPath;

    @JsonProperty("kafka_bootstrap_servers")
    private String kafkaBootstrapServers;

    @JsonProperty("kafka_schema_registry_URL")
    private String kafkaSchemaRegistryUrl;

    @Override
    public String toString() {
        return "ConfigValues [serverHost=" + serverHost + ", serverPort=" + serverPort + ", contextPath=" + contextPath
                + ", kafkaBootstrapServers=" + kafkaBootstrapServers + ", kafkaSchemaRegistryUrl=" + kafkaSchemaRegistryUrl + "]";
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
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
}
