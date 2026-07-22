package clients;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import utils.ConfigManager;

public class BaseClient {

    protected final RequestSpecification requestSpecification;

    public BaseClient() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.getBaseUrl())
                .addHeader("Authorization", "Bearer " + ConfigManager.getApiKey())
                .addHeader("Accept", "*/*")
                .build();
    }
}