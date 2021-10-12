package com.uday.sampleapp.controller;

import brave.Span;
import brave.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class HolaController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RestTemplate restTemplate;
    private static Map<String, String> env;


    @Value("${spring.security.user.name}")
    private String username;
    @Value("${spring.security.user.password}")
    private String password;

    @Value("${about.url}")
    private String aboutUrl;
    @Value("${location.url}")
    private String locationUrl;
    @Value("${joke.url}")
    private String jokeUrl;

    private final Tracer tracer;

    static {
        env = System.getenv();
    }

    @Autowired
    public HolaController(Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * Get startup message
     *
     * @return return welcome message
     */
    @RequestMapping(value = "/hola", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> hola(){
        try{
            List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

            boolean basicAuthInterceptorAlreadydded = interceptors.stream().anyMatch(
                    interceptor -> interceptor instanceof BasicAuthorizationInterceptor
            );

            // The interceptors are reused from one request to another request. So, we only 
            // want to append the auth header once.
            if (!basicAuthInterceptorAlreadydded) {
                interceptors.add(new BasicAuthorizationInterceptor(username, password));
            }

            String about = restTemplate.getForEntity(aboutUrl, String.class).getBody();
            String location = restTemplate.getForEntity(locationUrl, String.class).getBody();

            String joke = getAJoke();
            String dataCenter = env.get("DC_NAME");
            String cluster = env.get("CLUSTER_NAME");
            String verion = env.get("VERSION");
            String message = dataCenter + "-" + cluster + "-"+verion + "-"+"Welcome to Mesh!";

            HolaResponse holaResponse = new HolaResponse(message, about, location, joke);
            logger.info(holaResponse.toString());
            return new ResponseEntity<>(holaResponse, HttpStatus.OK);
        }
        catch (Exception ex) {
            String errorMessage = "Unable to process the request:\n"+ ex;
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getAJoke(){
        //start a new span
        Span parentSpan = tracer.newChild(tracer.currentSpan().context());
        parentSpan.tag("http.method", HttpMethod.GET.toString());
        parentSpan.tag("http.path", "/joke/getAJoke()");
        parentSpan.tag("mvc.controller.class", "JokeController");
        parentSpan.tag("mvc.controller.method", "joke");
        parentSpan.tag("peer.service", "");
        parentSpan.tag("peer.ipv4", "127.0.0.1");
        parentSpan.tag("peer.port", "37170");
        parentSpan.tag("span.kind", "server");

        try (Tracer.SpanInScope ws = tracer.withSpanInScope(parentSpan.start())) {
            //start a new span
            Span newSpan = tracer.newChild(tracer.currentSpan().context());
            newSpan.tag("http.method", HttpMethod.GET.toString());
            newSpan.tag("http.path", "/joke/getAJoke().doSomeRealWork()");
            newSpan.tag("mvc.controller.class", "JokeController");
            newSpan.tag("mvc.controller.method", "joke");
            newSpan.tag("peer.service", "");
            newSpan.tag("peer.ipv4", "127.0.0.1");
            newSpan.tag("peer.port", "37170");
            newSpan.tag("span.kind", "server");

            try (Tracer.SpanInScope s = tracer.withSpanInScope(newSpan.start())) {
                doSomeRealWork();
            }
            finally {
                newSpan.finish();
            }

            Map<String, String> params = new HashMap<>();
            String parametrizedArgs;
            if (Math.random() < 0.5) {
                params.put("type", "geek");
            } else {
                params.put("type", "dad");
            }
            parametrizedArgs = params.keySet().stream().map(k ->
                    String.format("%s={%s}", k, k)
            ).collect(Collectors.joining("&"));
            return restTemplate.exchange(String.format(jokeUrl, parametrizedArgs), HttpMethod.GET, null, String.class, params).getBody();
        }
        finally {
            parentSpan.finish();
        }
    }

    private void doSomeRealWork(){
        try {
            Thread.sleep(3000);
            Span newSpan = tracer.newChild(tracer.currentSpan().context());
            newSpan.tag("http.method", HttpMethod.GET.toString());
            newSpan.tag("http.path", "/joke/getAJoke().doSomeRealWork().postProcessingWork()");
            newSpan.tag("mvc.controller.class", "JokeController");
            newSpan.tag("mvc.controller.method", "joke");
            newSpan.tag("peer.service", "");
            newSpan.tag("peer.ipv4", "127.0.0.1");
            newSpan.tag("peer.port", "37170");
            newSpan.tag("span.kind", "server");
            try (Tracer.SpanInScope ws = tracer.withSpanInScope(newSpan.start())) {
                postProcessingWork();
            }
            finally {
                newSpan.finish();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void postProcessingWork(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class HolaResponse{
    private String welcome;
    private String about;
    private String location;
    private String joke;

    public HolaResponse(final String welcome, final String about, final String location, final String joke) {
        this.welcome = welcome;
        this.about = about;
        this.location = location;
        this.joke = joke;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public String toString(){
        return this.welcome + " "  + this.about + " " + this.location + " " + this.joke;
    }
}
