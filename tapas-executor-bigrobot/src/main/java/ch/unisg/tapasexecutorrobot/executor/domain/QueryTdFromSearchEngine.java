package ch.unisg.tapasexecutorrobot.executor.domain;

import ch.unisg.ics.interactions.wot.td.ThingDescription;
import ch.unisg.ics.interactions.wot.td.io.TDGraphReader;
import ch.unisg.tapascommon.communication.WebClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Optional;

public class QueryTdFromSearchEngine {

    private static final Logger LOGGER = LogManager.getLogger(QueryTdFromSearchEngine.class);

    private final static String SEARCH_ENGINE_API = "https://api.interactions.ics.unisg.ch/search/searchEngine";

    private final static String SPARQL_QUERY = "@prefix td: <https://www.w3.org/2019/wot/td#>.\n" +
            "select ?x\n" +
            "where { ?x a td:Thing }";

    public static Optional<ThingDescription> getTd(String thingName) {
        try {
            var response = WebClient.post(SEARCH_ENGINE_API, SPARQL_QUERY, "application/sparql-query");

            var xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                    new InputSource(new StringReader(response.body().replace("\n", ""))));
            xml.normalize();
            var resultElements = xml.getElementsByTagName("result");

            for (int i = 0; i < resultElements.getLength(); i++) {
                String retrievedUri = resultElements.item(i).getFirstChild().getFirstChild().getTextContent();
                if (retrievedUri.contains(thingName)) {
                    return Optional.of(TDGraphReader.readFromURL(ThingDescription.TDFormat.RDF_TURTLE, retrievedUri));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to get TD for " + thingName);
            LOGGER.warn(e);
        }
        return Optional.empty();
    }
}
