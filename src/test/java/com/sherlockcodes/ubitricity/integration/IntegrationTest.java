package com.sherlockcodes.ubitricity.integration;

import com.sherlockcodes.ubitricity.enums.ChargeMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    private static final Logger logger = LogManager.getLogger(IntegrationTest.class);
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final HttpHeaders headers = new HttpHeaders();
    private HttpEntity entity;

    @LocalServerPort
    private int port;

    @Before
    public void set() {
        entity = new HttpEntity(null, headers);
    }

    @Test
    public void testPark() throws Exception {


        getStringResponseEntity(HttpMethod.POST, "/station/car/1");
        getStringResponseEntity(HttpMethod.POST, "/station/car/2");
        getStringResponseEntity(HttpMethod.POST, "/station/car/3");
        getStringResponseEntity(HttpMethod.POST, "/station/car/4");
        getStringResponseEntity(HttpMethod.POST, "/station/car/5");

        getStringResponseEntity(HttpMethod.DELETE, "/station/car/3");

        validate(new int[]{0, 20, 20, 0, 20, 20, 0, 0, 0, 0, 0});

        getStringResponseEntity(HttpMethod.POST, "/station/car/8");
        validate(new int[]{0, 20, 20, 0, 20, 20, 0, 0, 20, 0, 0});
        logger.info("No power changes till now");
        getStringResponseEntity(HttpMethod.POST, "/station/car/6");
        validate(new int[]{0, 10, 10, 0, 20, 20, 20, 0, 20, 0, 0});
        logger.info("power diverted from old cars to new one");
        getStringResponseEntity(HttpMethod.POST, "/station/car/7");
        validate(new int[]{0, 10, 10, 0, 10, 10, 20, 20, 20, 0, 0});

        getStringResponseEntity(HttpMethod.DELETE, "/station/car/6");
        validate(new int[]{0, 10, 10, 0, 20, 20, 0, 20, 20, 0, 0});
        logger.info("power given back to 2 slow charging cars");

        getStringResponseEntity(HttpMethod.DELETE, "/station/car/1");
        validate(new int[]{0, 0, 20, 0, 20, 20, 0, 20, 20, 0, 0});

    }

    private void validate(int[] ints) {
        ConcurrentHashMap map = restTemplate.exchange(
                createURLWithPort("/station/status"), HttpMethod.GET, entity, ConcurrentHashMap.class).getBody();
        for (int i = 1; i <= 10; i++) {
            assertEquals(map.get(i + "") ,transform(ints[i]));
        }
    }

    private String transform(int anInt) {
        switch (anInt) {
            case 0: return ChargeMode.AVAILABLE.getText();
            case 10:return ChargeMode.SLOW_CHARGE.getText();
            case 20:return ChargeMode.FAST_CHARGE.getText();
            default: return "" ;
        }
    }


    private void getStringResponseEntity(HttpMethod method, String uri) {
        restTemplate.exchange(createURLWithPort(uri), method, entity, String.class);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}