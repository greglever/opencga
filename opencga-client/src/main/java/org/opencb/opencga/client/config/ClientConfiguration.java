/*
 * Copyright 2015-2017 OpenCB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opencb.opencga.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.opencb.commons.utils.FileUtils;
import org.opencb.opencga.core.models.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;

/**
 * Created by imedina on 04/05/16.
 */
public class ClientConfiguration {

    private String logLevel;
    private String logFile;

    private String version;
    private int sessionDuration;

    private Project.Organism organism;
    private String defaultStudy;

    private Map<String, String> alias;

    private RestConfig rest;
    private GrpcConfig grpc;

    private VariantClientConfiguration variant;

    private static Logger logger;

    private static final String DEFAULT_CONFIGURATION_FORMAT = "yaml";

    static {
        logger = LoggerFactory.getLogger(ClientConfiguration.class);
    }

    public ClientConfiguration() {
    }

    public ClientConfiguration(RestConfig rest, GrpcConfig grpc) {
        this.rest = rest;
        this.grpc = grpc;
    }

    public static ClientConfiguration load(Path configurationPath) throws IOException {
        InputStream inputStream = FileUtils.newInputStream(configurationPath);
        return load(inputStream, DEFAULT_CONFIGURATION_FORMAT);
    }

    public static ClientConfiguration load(InputStream configurationInputStream) throws IOException {
        return load(configurationInputStream, DEFAULT_CONFIGURATION_FORMAT);
    }

    public static ClientConfiguration load(InputStream configurationInputStream, String format) throws IOException {
        ClientConfiguration clientConfiguration;
        ObjectMapper objectMapper;
        switch (format) {
            case "json":
                objectMapper = new ObjectMapper();
                clientConfiguration = objectMapper.readValue(configurationInputStream, ClientConfiguration.class);
                break;
            case "yml":
            case "yaml":
            default:
                objectMapper = new ObjectMapper(new YAMLFactory());
                clientConfiguration = objectMapper.readValue(configurationInputStream, ClientConfiguration.class);
                break;
        }
        overwriteEnvironmentVariables(clientConfiguration);
        return clientConfiguration;
    }

    public void serialize(OutputStream configurationOutputStream) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper(new YAMLFactory());
        jsonMapper.writerWithDefaultPrettyPrinter().writeValue(configurationOutputStream, this);
    }

    private static void overwriteEnvironmentVariables(ClientConfiguration configuration) {
        Map<String, String> envVariables = System.getenv();
        for (String variable : envVariables.keySet()) {
            if (variable.startsWith("OPENCGA_")) {
                logger.debug("Overwriting environment parameter '{}'", variable);
                switch (variable) {
                    case "OPENCGA_CLIENT_ORGANISM_TAXONOMY_CODE":
                        configuration.getOrganism().setTaxonomyCode(Integer.parseInt(envVariables.get(variable)));
                        break;
                    case "OPENCGA_CLIENT_ORGANISM_SCIENTIFIC_NAME":
                        configuration.getOrganism().setScientificName(envVariables.get(variable));
                        break;
                    case "OPENCGA_CLIENT_ORGANISM_COMMON_NAME":
                        configuration.getOrganism().setCommonName(envVariables.get(variable));
                        break;
                    case "OPENCGA_CLIENT_ORGANISM_ASSEMBLY":
                        configuration.getOrganism().setAssembly(envVariables.get(variable));
                        break;
                    case "OPENCGA_CLIENT_REST_HOST":
                        configuration.getRest().setHost(envVariables.get(variable));
                        break;
                    case "OPENCGA_CLIENT_REST_TIMEOUT":
                        configuration.getRest().setTimeout(Integer.valueOf(envVariables.get(variable)));
                        break;
                    case "OPENCGA_CLIENT_GRPC_HOST":
                        configuration.getGrpc().setHost(envVariables.get(variable));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClientConfiguration{");
        sb.append("logLevel='").append(logLevel).append('\'');
        sb.append(", logFile='").append(logFile).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", sessionDuration=").append(sessionDuration);
        sb.append(", organism=").append(organism);
        sb.append(", defaultStudy='").append(defaultStudy).append('\'');
        sb.append(", alias=").append(alias);
        sb.append(", rest=").append(rest);
        sb.append(", grpc=").append(grpc);
        sb.append(", variant=").append(variant);
        sb.append('}');
        return sb.toString();
    }

    public String getLogLevel() {
        return logLevel;
    }

    public ClientConfiguration setLogLevel(String logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public String getLogFile() {
        return logFile;
    }

    public ClientConfiguration setLogFile(String logFile) {
        this.logFile = logFile;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public ClientConfiguration setVersion(String version) {
        this.version = version;
        return this;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public ClientConfiguration setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
        return this;
    }

    public Project.Organism getOrganism() {
        return organism;
    }

    public ClientConfiguration setOrganism(Project.Organism organism) {
        this.organism = organism;
        return this;
    }

    public String getDefaultStudy() {
        return defaultStudy;
    }

    public ClientConfiguration setDefaultStudy(String defaultStudy) {
        this.defaultStudy = defaultStudy;
        return this;
    }

    public Map<String, String> getAlias() {
        return alias;
    }

    public ClientConfiguration setAlias(Map<String, String> alias) {
        this.alias = alias;
        return this;
    }

    public RestConfig getRest() {
        return rest;
    }

    public ClientConfiguration setRest(RestConfig rest) {
        this.rest = rest;
        return this;
    }

    public GrpcConfig getGrpc() {
        return grpc;
    }

    public ClientConfiguration setGrpc(GrpcConfig grpc) {
        this.grpc = grpc;
        return this;
    }

    public VariantClientConfiguration getVariant() {
        return variant;
    }

    public ClientConfiguration setVariant(VariantClientConfiguration variant) {
        this.variant = variant;
        return this;
    }
}
