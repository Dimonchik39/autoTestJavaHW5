import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AutocompleteSearchTest extends AbstractTest {

    private static final Logger logger
            = LoggerFactory.getLogger(AutocompleteSearchTest.class);

    @Test
    void autoSearchTest() throws IOException, URISyntaxException {
        logger.info("Тест: Автопоиск по части значений");
        logger.debug("Формирование мока для GET /locations/v1/cities/autocomplete");
        stubFor(get(urlPathEqualTo("/locations/v1/cities/autocomplete"))
                .withQueryParam("q", equalTo("Kalinin"))
                .willReturn(aResponse()
                        .withStatus(200).withBody("Kaliningrad")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/cities/autocomplete");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("q", "Kalinin")
                .build();
        request.setURI(uri);
        logger.debug("http клиент создан");
        HttpResponse response = httpClient.execute(request);
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/cities/autocomplete")));
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Kaliningrad", convertResponseToString(response));
    }
}
