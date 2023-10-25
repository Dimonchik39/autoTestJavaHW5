import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.Location;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CitySearchKaliningradTest  extends AbstractTest {

    private static final Logger logger
            = LoggerFactory.getLogger(CitySearchKaliningradTest.class);

    @Test
    void searchCityKaliningrad() throws IOException, URISyntaxException {
        logger.info("Тест: Поиск города Калининград");

        ObjectMapper mapper = new ObjectMapper();
        Location bodySearch = new Location();
        bodySearch.setKey("Kaliningrad");

        logger.debug("Формирование мока для GET /locations/v1/cities/search");
        stubFor(get(urlPathEqualTo("/locations/v1/cities/search"))
                .withQueryParam("q", equalTo("Kaliningrad"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodySearch))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");

        HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/cities/search");
        URI uriSearch = new URIBuilder(request.getURI())
                .addParameter("q", "Kaliningrad")
                .build();
        request.setURI(uriSearch);
        HttpResponse responseOk = httpClient.execute(request);

        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals("Kaliningrad", mapper.readValue(responseOk.getEntity().getContent(), Location.class).getKey());
    }

    @Test
    void returnCode500() throws IOException {
        logger.info("Тест: Код 500");
        logger.debug("Формирование мока для GET /locations/v1/cities/search");
        stubFor(get(urlPathEqualTo("/locations/v1/cities/search"))
                .willReturn(aResponse()
                        .withStatus(500).withBody("ERROR")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/cities/search");
        logger.debug("http клиент создан");
        HttpResponse response = httpClient.execute(request);
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/cities/search")));
        assertEquals(500, response.getStatusLine().getStatusCode());
        assertEquals("ERROR", convertResponseToString(response));
    }
}
