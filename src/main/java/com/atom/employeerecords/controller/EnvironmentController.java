package com.atom.employeerecords.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EnvironmentController {

    @Value("${deployment.environment:unknown}")
    private String deploymentEnv;

    @GetMapping("/test-environment")
    public ResponseEntity<Map<String, Object>> testEnvironment() {
        Map<String, Object> response = new HashMap<>();

        if (deploymentEnv == null || deploymentEnv.trim().isEmpty() || "unknown".equals(deploymentEnv)) {
            response.put("status", "error");
            response.put("message", "No environment variable set. Please configure DEPLOYMENT_ENV.");
            return ResponseEntity.ok(response);
        }

        switch (deploymentEnv.toLowerCase()) {
            case "prod":
                response.put("environment", "production");
                response.put("status", "success");
                response.put("message", "Running in PRODUCTION environment");
                response.put("data", Map.of(
                    "version", "1.0.0",
                    "isProduction", true
                ));
                break;

            case "dev":
                response.put("environment", "development");
                response.put("status", "success");
                response.put("message", "Running in DEVELOPMENT environment");
                response.put("deployment", "dev");
                break;

            case "qa":
                response.put("environment", "qa");
                response.put("status", "success");
                response.put("message", "Running in QA environment");
                response.put("deployment", "qa");
                break;

            default:
                response.put("status", "warning");
                response.put("message", "Unknown environment: " + deploymentEnv);
                response.put("availableEnvironments", new String[]{"prod", "dev", "qa"});
        }

        return ResponseEntity.ok(response);
    }
}
