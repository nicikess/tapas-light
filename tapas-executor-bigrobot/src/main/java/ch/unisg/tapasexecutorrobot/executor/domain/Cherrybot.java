package ch.unisg.tapasexecutorrobot.executor.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// https://app.swaggerhub.com/apis-docs/interactions-hsg/robots/1.0.0#/
public class Cherrybot {

    private static final Logger LOGGER = LogManager.getLogger(Cherrybot.class);

    private static final String ROBOT_API = "https://api.interactions.ics.unisg.ch/cherrybot/";

    private static final String OPERATOR_NAME = "Group 4";
    private static final String OPERATOR_EMAIL = "group4@unisg.ch";

    @Getter
    @Setter
    private String operatorToken = "";

    public boolean postOperator() {
        LOGGER.info("Post Operator");

        var json = "{\n" +
                "  \"name\": \"" + OPERATOR_NAME + "\",\n" +
                "  \"email\": \"" + OPERATOR_EMAIL + "\"" +
                "}";
        var response = sendRestRequest("operator", "post", json);

        var ok = checkResponseStatusCode(response);
        if (ok) {
            var locationSplit = response.headers().map().get("location").get(0).split("/");
            operatorToken = locationSplit[locationSplit.length - 1];
            LOGGER.info("Token: " + operatorToken);
        }

        return ok;
    }

    public boolean deleteOperator() {
        LOGGER.info("Delete Operator");
        var response = sendRestRequest("operator/" + operatorToken, "delete", null);
        return checkResponseStatusCode(response);
    }

    public boolean postInitialize() {
        LOGGER.info("Post Initialize");
        var response = sendRestRequest("initialize", "put", "");
        return checkResponseStatusCode(response);
    }

    public boolean putTcp() {
        LOGGER.info("Put TCP");
        var json = "{\n" +
                "  \"target\": {\n" +
                "    \"coordinate\": {\n" +
                "      \"x\": 400,\n" +
                "      \"y\": 0,\n" +
                "      \"z\": 400\n" +
                "    },\n" +
                "    \"rotation\": {\n" +
                "      \"roll\": 180,\n" +
                "      \"pitch\": 0,\n" +
                "      \"yaw\": 0\n" +
                "    }\n" +
                "  },\n" +
                "  \"speed\": 50\n" +
                "}";
        var response = sendRestRequest("tcp/target", "put", json);
        return checkResponseStatusCode(response);
    }

    private boolean checkResponseStatusCode(HttpResponse<String> response) {
        var ok = response != null && response.statusCode() >= 200 && response.statusCode() < 300;

        if (ok) {
            LOGGER.info("REST Operation successful");
        } else {
            LOGGER.warn("REST Operation failed");
        }

        return ok;
    }

    private HttpResponse<String> sendRestRequest(String path, String operation, String payload) {
        var client = HttpClient.newHttpClient();

        var requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(ROBOT_API + path));

        if (operation.equals("post")) {
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(payload))
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json");;
        } else if (operation.equals("put")) {
            requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(payload))
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json");;
        } else if (operation.equals(("delete"))) {
            requestBuilder.DELETE();
        }

        if (!operatorToken.isEmpty()) {
            requestBuilder.setHeader("Authentication", operatorToken);
        }

        var request = requestBuilder.build();

        HttpResponse<String> response = null;

        try {
            response =  client.send(request, HttpResponse.BodyHandlers.ofString());

            LOGGER.debug("Response Status Code: " + response.statusCode());
            LOGGER.debug("Response Body: " + response.body());

            Thread.sleep(1000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return response;
    }
}
