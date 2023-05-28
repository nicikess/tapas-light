package ch.unisg.tapasexecutorrobot.executor.domain;

import ch.unisg.ics.interactions.wot.td.clients.TDHttpRequest;
import ch.unisg.ics.interactions.wot.td.clients.TDHttpResponse;
import ch.unisg.ics.interactions.wot.td.schemas.ObjectSchema;
import ch.unisg.ics.interactions.wot.td.security.APIKeySecurityScheme;
import ch.unisg.ics.interactions.wot.td.vocabularies.TD;
import ch.unisg.ics.interactions.wot.td.vocabularies.WoTSec;
import ch.unisg.tapascommon.communication.WebClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

public class CherrybotWot {

    private static final Logger LOGGER = LogManager.getLogger(CherrybotWot.class);

    private static final String THING_NAME = "cherrybot";

    private static final String ACTION_REGISTER_OPERATOR = "registerOperator";
    private static final String ACTION_TCP_SET_TARGET = "setTarget";
    private static final String DELETE_OPERATOR_ENDPOINT = "/operator/";

    private static final String OPERATOR_NAME = "Group 4";
    private static final String OPERATOR_EMAIL = "group4@unisg.ch";

    @Getter
    @Setter
    private String operatorToken = "";

    public boolean registerOperator() {
        LOGGER.info("Register operator");

        var payload = new HashMap<String, Object>();
        payload.put("name", OPERATOR_NAME);
        payload.put("email", OPERATOR_EMAIL);
        var response = sendWotRequest(ACTION_REGISTER_OPERATOR, payload);

        var ok = checkResponseStatusCode(response);

        if (ok) {
            var locationSplit = response.getHeaders().get("location").split("/");
            operatorToken = locationSplit[locationSplit.length - 1];
            LOGGER.info("Token: " + operatorToken);
        } else {
            if (response == null) {
                return false;
            }
            LOGGER.info("Failed to retrieve operator token");
            LOGGER.info(response.getPayload().get());
        }
        return ok;
    }

    public boolean removeOperator() {
        LOGGER.info("Remove operator");
        var td = QueryTdFromSearchEngine.getTd(THING_NAME);
        if (td.isEmpty()) {
            LOGGER.warn("Failed to retrieve TD");
            return false;
        }
        if (td.get().getBaseURI().isEmpty()) {
            LOGGER.warn("Failed to retrieve TD base uri");
            return false;
        }
        var uri = td.get().getBaseURI().get();
        try {
            var response = WebClient.delete(uri + DELETE_OPERATOR_ENDPOINT + operatorToken, null, null);
            if (WebClient.checkResponseStatusCode(response)) {
                LOGGER.info("Successfully removed operator");
                return true;
            } else {
                LOGGER.warn("Failed to remove operator");
                return false;
            }
        } catch (IOException | InterruptedException ignored) {
            LOGGER.warn("Failed to send delete operator request");
        }
        return false;
    }

    public boolean initialize() {
        LOGGER.info("Initialize");
        var response = sendWotRequest("initialize", null);
        return checkResponseStatusCode(response);
    }

    public boolean tcpSetTarget() {
        LOGGER.info("TCP set target");

        var coords = new HashMap<String, Object>();
        coords.put("x", 400);
        coords.put("y", 0);
        coords.put("z", 400);

        var rotation = new HashMap<String, Object>();
        rotation.put("yaw", 0);
        rotation.put("roll", 0);
        rotation.put("pitch", 180);

        var target = new HashMap<String, Object>();
        target.put("coordinate", coords);
        target.put("rotation", rotation);

        var payload = new HashMap<String, Object>();
        payload.put("target", target);
        payload.put("speed", 50);

        var response = sendWotRequest(ACTION_TCP_SET_TARGET, payload);
        return checkResponseStatusCode(response);
    }

    private TDHttpResponse sendWotRequest(String actionName, HashMap<String, Object> payload) {
        var td = QueryTdFromSearchEngine.getTd(THING_NAME);
        if (td.isEmpty()) {
            LOGGER.warn("Failed to retrieve TD");
            return null;
        }
        var action = td.get().getActionByName(actionName);
        if (action.isEmpty()) {
            LOGGER.warn("Failed to retrieve TD action");
            return null;
        }
        var form = action.get().getFirstForm();
        if (form.isEmpty()) {
            LOGGER.warn("Failed to retrieve TD action form");
            return null;
        }
        var request = new TDHttpRequest(form.get(), TD.invokeAction);
        if (payload != null) {
            var inputSchema = action.get().getInputSchema();
            if (inputSchema.isEmpty()) {
                LOGGER.warn("Failed to retrieve TD action input schema");
                return null;
            }
            request.setObjectPayload((ObjectSchema) inputSchema.get(), payload);
        }
        var securityScheme = td.get().getFirstSecuritySchemeByType(WoTSec.APIKeySecurityScheme);
        if (securityScheme.isPresent() && !operatorToken.isEmpty()) {
            request.setAPIKey((APIKeySecurityScheme) securityScheme.get(), operatorToken);
        }
        TDHttpResponse response = null;
        try {
            response = request.execute();
            LOGGER.info("Received response payload: " + response.getPayload().get());
            Thread.sleep(1000);
        }
        catch (IOException | InterruptedException e) {
            LOGGER.warn("Failed execute TD action " + actionName);
        }
        return response;
    }

    private boolean checkResponseStatusCode(TDHttpResponse response) {
        if (response == null) {
            return false;
        }

        var ok = response.getStatusCode() >= 200 && response.getStatusCode() < 300;

        if (ok) {
            LOGGER.info("WoT Operation successful " + response.getStatusCode());
        } else {
            LOGGER.warn("WoT Operation failed with status code " + response.getStatusCode());
        }

        return ok;
    }
}
