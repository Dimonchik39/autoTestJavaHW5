import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TopCitiesListStatucCodeTest extends AbstractTest {
    private static final Logger logger
            = LoggerFactory.getLogger(TopCitiesListStatucCodeTest.class);

    @Test
    void returnCode500() throws IOException {
        logger.info("Тест: Код 500");
        logger.debug("Формирование мока для GET /locations/v1/topcities/50");
        stubFor(get(urlPathEqualTo("/locations/v1/topcities/50"))
                .willReturn(aResponse()
                        .withStatus(500).withBody("ERROR")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/topcities/50");
        logger.debug("http клиент создан");
        HttpResponse response = httpClient.execute(request);
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/topcities/50")));
        assertEquals(500, response.getStatusLine().getStatusCode());
        assertEquals("ERROR", convertResponseToString(response));
    }

    @Test
    void returnCode200() throws IOException {
        logger.info("Тест: Код 200");
        logger.debug("Формирование мока для GET /locations/v1/topcities/50");
        stubFor(get(urlPathEqualTo("/locations/v1/topcities/50"))
                .willReturn(aResponse()
                        .withStatus(200).withBody("OK")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/topcities/50");
        logger.debug("http клиент создан");
        HttpResponse response = httpClient.execute(request);
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/topcities/50")));
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("OK", convertResponseToString(response));
    }
}
