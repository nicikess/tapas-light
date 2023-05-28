package ch.unisg.tapasexecutortemperature.executor.application.service;

import ch.unisg.ics.interactions.wot.td.ThingDescription;
import ch.unisg.ics.interactions.wot.td.affordances.Form;
import ch.unisg.ics.interactions.wot.td.affordances.PropertyAffordance;
import ch.unisg.ics.interactions.wot.td.clients.TDCoapRequest;
import ch.unisg.ics.interactions.wot.td.clients.TDCoapResponse;
import ch.unisg.ics.interactions.wot.td.io.TDGraphReader;
import ch.unisg.ics.interactions.wot.td.vocabularies.TD;
import ch.unisg.tapascommon.tasks.domain.Task;
import ch.unisg.tapasexecutorbase.executor.application.port.in.ExecuteTaskCommand;
import ch.unisg.tapasexecutorbase.executor.application.port.in.ExecuteTaskUseCase;
import ch.unisg.tapasexecutorbase.executor.application.port.out.ExecutorStateChangedEventPort;
import ch.unisg.tapasexecutorbase.executor.application.port.out.TaskUpdatedEventPort;
import ch.unisg.tapasexecutorbase.executor.application.service.ExecuteTaskBaseService;
import ch.unisg.tapasexecutorbase.executor.config.ExecutorConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Primary
@Component
public class ExecuteTaskService implements ExecuteTaskUseCase {

    private static final Logger LOGGER = LogManager.getLogger(ExecuteTaskService.class);

    private final ExecutorConfig executorConfig;
    private final ExecutorStateChangedEventPort executorStateChangedEventPort;
    private final TaskUpdatedEventPort taskUpdatedEventPort;

    @Override
    public void executeTask(ExecuteTaskCommand command) {
        var executorBaseService = new ExecuteTaskBaseService(
                executorConfig, executorStateChangedEventPort, taskUpdatedEventPort
        );

        executorBaseService.updateExecutorState("BUSY");

        var task = command.getTask();

        task.setTaskStatus(new Task.TaskStatus(Task.Status.RUNNING));
        executorBaseService.updateTaskStatus(task);

        var temperature = retrieveTemperature();
        LOGGER.info("Temperature: " + temperature);

        task.setOutputData(new Task.OutputData(temperature));
        task.setTaskStatus(new Task.TaskStatus(Task.Status.EXECUTED));
        executorBaseService.updateTaskStatus(task);

        executorBaseService.updateExecutorState("IDLE");
    }

    public String retrieveTemperature() {
        var uriMiro = getURIForMiroCard();
        if (uriMiro.isPresent()) {
            var formOptional = getForm(uriMiro.get());
            if (formOptional.isPresent()) {
                return getTemp(formOptional);
            }
        }
        return "FAILED";
    }

    private static Optional<String> getURIForMiroCard() {

        final String ENTRY_POINT = "https://api.interactions.ics.unisg.ch/search/searchEngine";

        final String SPARQL_QUERY = "@prefix td: <https://www.w3.org/2019/wot/td#>.\n" +
                "select ?x\n" +
                "where { ?x a td:Thing }";

        final String KEYWORD = "mirogate";

        HttpClient client = HttpClient.newHttpClient();

        LOGGER.info("Query with: " + KEYWORD);

        HttpRequest qlRequest = HttpRequest.newBuilder()
                .uri(URI.create(ENTRY_POINT))
                .header("Content-Type", "application/sparql-query")
                .POST(HttpRequest.BodyPublishers.ofString(SPARQL_QUERY))
                .build();

        LOGGER.info("Finished querying");

        Optional<String> miroURI = Optional.empty();

        try {
            HttpResponse response = client.send(qlRequest, HttpResponse.BodyHandlers.ofString());
            Document doc = convertStringToXMLDocument(response.body().toString());
            doc.getDocumentElement().normalize();
            NodeList resultElements = doc.getElementsByTagName("result");

            for (int i = 0; i < resultElements.getLength(); i++) {
                String retrievedUri = resultElements.item(i).getFirstChild().getFirstChild().getTextContent();
                if (retrievedUri.contains(KEYWORD)) {
                    miroURI = Optional.of(retrievedUri);
                    LOGGER.info("URI for miro: " + retrievedUri);
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Query failed");
            e.printStackTrace();
        }

        return miroURI;
    }

    private static Document convertStringToXMLDocument(String xmlString)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString.replace("\n", ""))));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private static Optional<Form> getForm(String uriMiro) {
        Optional<Form> result = Optional.empty();
        try {
            ThingDescription td = TDGraphReader.readFromURL(ThingDescription.TDFormat.RDF_TURTLE, uriMiro.toString());
            Optional<PropertyAffordance> propertyAffordance = td.getPropertyByName("temperature");
            if (propertyAffordance.isPresent()) {
                Optional<Form> form = propertyAffordance.get().getFirstFormForOperationType(TD.readProperty);
                result = form;
            }
        } catch (Exception e) {
            LOGGER.info("Could not fetch information from Miro TD");
            e.printStackTrace();
        }

        return result;

    }

    private static String getTemp(Optional<Form> formOptional) {
        ObjectMapper objectMapper = new ObjectMapper();
        String temp = null;

        if (formOptional.isPresent()) {
            Form form = formOptional.get();
            TDCoapRequest request = new TDCoapRequest(form, TD.readProperty);
            try {
                TDCoapResponse response = request.execute();
                if (response.getPayload().isEmpty()) {
                    LOGGER.info("No payload from CoAP received");
                }
                temp = objectMapper.readValue(response.getPayload().get(), JsonNode.class).get("value").toString();
            }
            catch (Exception e) {
                LOGGER.info("Failed receiving CoAP response");
            }
        }
        return temp;
    }
}
