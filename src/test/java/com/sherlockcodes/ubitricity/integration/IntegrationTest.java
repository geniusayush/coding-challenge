package com.sherlockcodes.ubitricity.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;

    @Test
    public void testCreateStudent() throws Exception {

        HttpEntity entity = new HttpEntity(null, headers);

        getStringResponseEntity(entity, "/station/car/1");
        getStringResponseEntity(entity, "/station/car/2");
        getStringResponseEntity(entity, "/station/car/3");
        getStringResponseEntity(entity, "/station/car/4");
        getStringResponseEntity(entity, "/station/car/5");

        ResponseEntity<ConcurrentHashMap> response = restTemplate.exchange(
                createURLWithPort("/station/status"), HttpMethod.GET, entity, ConcurrentHashMap.class);
        ConcurrentHashMap plan = response.getBody();
        assert plan != null;


    }

    @Test
    public void testCreateStudent2() throws Exception {

        HttpEntity entity = new HttpEntity(null, headers);

        getStringResponseEntity(entity, "/station/1");
        getStringResponseEntity(entity, "/station/2");
        getStringResponseEntity(entity, "/station/3");
        getStringResponseEntity(entity, "/station/4");
        getStringResponseEntity(entity, "/station/5");
        restTemplate.exchange(
                createURLWithPort("/station/4"), HttpMethod.DELETE, entity, String.class);
        ResponseEntity<ConcurrentHashMap> response = restTemplate.exchange(
                createURLWithPort("/station/status"), HttpMethod.POST, entity, ConcurrentHashMap.class);
        ConcurrentHashMap plan = response.getBody();
        assert plan != null;


    }

    private ResponseEntity<String> getStringResponseEntity(HttpEntity entity, String uri) {
        return restTemplate.exchange(
                createURLWithPort(uri), HttpMethod.POST, entity, String.class);
    }


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}