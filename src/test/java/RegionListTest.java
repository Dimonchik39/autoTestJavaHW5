import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.Country;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegionListTest extends AbstractTest{

    private static final Logger logger
            = LoggerFactory.getLogger(RegionListTest.class);

    @Test
    void searchRegion() throws IOException, URISyntaxException {
        logger.info("Тест: Поиск регионов");

        ObjectMapper mapper = new ObjectMapper();
        Country bodySearch = new Country();
        bodySearch.setLocalizedName("AFR");

        logger.debug("Формирование мока для GET /locations/v1/regions");
        stubFor(get(urlPathEqualTo("/locations/v1/regions"))
                .withQueryParam("apiKey", equalTo("666"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodySearch))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http клиент создан");

        HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/regions");
        URI uriSearch = new URIBuilder(request.getURI())
                .addParameter("apiKey", "666")
                .build();
        request.setURI(uriSearch);
        HttpResponse responseOk = httpClient.execute(request);

        assertEquals(200, responseOk.getStatusLine().getStatusCode());
        assertEquals("AFR", mapper.readValue(responseOk.getEntity().getContent(), Country.class).getLocalizedName());
    }
}
