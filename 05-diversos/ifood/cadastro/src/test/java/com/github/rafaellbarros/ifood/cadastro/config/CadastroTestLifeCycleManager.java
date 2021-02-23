package com.github.rafaellbarros.ifood.cadastro.config;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

public class CadastroTestLifeCycleManager implements QuarkusTestResourceLifecycleManager {

    public static final PostgreSQLContainer<?> POTGRES = new PostgreSQLContainer<>("postgres:13.0");

    @Override
    public Map<String, String> start() {
        POTGRES.start();
        Map<String, String> propriedades = new HashMap<>();
        propriedades.put("quarkus.datasource.jdbc.url", POTGRES.getJdbcUrl());
        propriedades.put("quarkus.datasource.username", POTGRES.getUsername());
        propriedades.put("quarkus.datasource.password", POTGRES.getPassword());
        return propriedades;
    }

    @Override
    public void stop() {
        if(nonNull(POTGRES) && POTGRES.isRunning()) {
            POTGRES.stop();
        }
    }
}
