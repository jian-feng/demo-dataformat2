package com.redhat.sample;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(CamelSpringBootRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { "management.port=0" })
public class CamelRouteUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CamelRouteUnitTest.class);

    /**
     * Camel Contextをインジェクションする。
     */
    @Autowired
    CamelContext camelContext;

    @Test
    public void testDynamicUnmarshal() throws Exception {

        ProducerTemplate template = camelContext.createProducerTemplate();

        // define what dataformat we want to use
        String dataformat_name = "json1";
        // send to direct uri to run test
        Object response = template.requestBodyAndHeader(
                "direct:dynamic-unmarshal",                    // to uri
                "{\"field1\": \"AAA\", \"field2\": \"111\"}",  // body
                "dataformat_name",                             // header name
                dataformat_name                                // header value
                );
        LOG.info("response is {}", response);

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(Request.class);
    }

    @Test
    public void testDynamicMarshal() throws Exception {

        ProducerTemplate template = camelContext.createProducerTemplate();

        // define what dataformat we want to use
        String dataformat_name = "json1";

        // define request as POJO
        Request requestObject = new Request("AAA", "111");
        // send to direct uri to run test
        Object response = template.requestBodyAndHeader(
                "direct:dynamic-marshal",               // to uri
                requestObject,                          // body
                "dataformat_name",                      // header name
                dataformat_name                         // header value
                );
        LOG.info("response is {}", response);

        assertThat(response).isNotNull();

        // check response JSON could be convert to POJO
        ObjectMapper mapper = new ObjectMapper();
        Request obj = mapper.readValue(response.toString(),
                Request.class);
        assertThat(obj).isNotNull();
    }

    @Test
    public void testPerformace10000() throws Exception {

        ProducerTemplate template = camelContext.createProducerTemplate();
        runWithLoop(template, 10000);

    }

    @Test
    public void testPerformace100000() throws Exception {

        ProducerTemplate template = camelContext.createProducerTemplate();
        runWithLoop(template, 100000);

    }

    private void runWithLoop(ProducerTemplate template, int loopLimit){
        // define what dataformat we want to use
        String dataformat_name = "json1";

        // define request as POJO
        Request requestObject = new Request("AAA", "111");

        for (int i = 0; i < loopLimit; i++) {
            template.requestBodyAndHeader(
                "direct:dynamic-unmarshal",                    // to uri
                "{\"field1\": \"AAA\", \"field2\": \"111\"}",  // body
                "dataformat_name",                             // header name
                dataformat_name                                // header value
                );
            template.requestBodyAndHeader(
                "direct:dynamic-marshal",               // to uri
                requestObject,                          // body
                "dataformat_name",                      // header name
                dataformat_name                         // header value
                );
        }

    }


}
