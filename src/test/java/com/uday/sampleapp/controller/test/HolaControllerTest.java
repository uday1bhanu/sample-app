package com.uday.sampleapp.controller.test;

import com.uday.sampleapp.SampleAppApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT, classes={ SampleAppApplication.class })
public class HolaControllerTest {
    @Value("${local.server.port}")
    private int port;
    private URL base;
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/hola");
        template = new TestRestTemplate().withBasicAuth("ap", "apapp!");
    }

    @Test
    public void shouldGetHolaMessage() {
        ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getHolaShouldSendResponseHeaders() {
        ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        HttpHeaders httpHeaders = response.getHeaders();
        assertEquals("nosniff", httpHeaders.get("X-Content-Type-Options").get(0));
        assertEquals("1; mode=block", httpHeaders.get("X-XSS-Protection").get(0));
        assertEquals("no-cache, no-store, max-age=0, must-revalidate", httpHeaders.getCacheControl());
        assertEquals("no-cache", httpHeaders.getPragma());
    }

    @After
    public void tearDown() {
        this.base = null;
        template = null;
    }
}
