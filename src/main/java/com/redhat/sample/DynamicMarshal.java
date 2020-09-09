package com.redhat.sample;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.converter.stream.OutputStreamBuilder;
import org.apache.camel.impl.DefaultRouteContext;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("dynamicMarshal")
public class DynamicMarshal implements Processor {

  @Override
  public void process(Exchange exchange) throws Exception {

    String dataFormatName = exchange.getIn().getHeader("dataformat_name", String.class);

    if (StringUtils.isEmpty(dataFormatName))
      throw new IllegalArgumentException("dataformat_name is empty.");

    CamelContext camelContext = exchange.getContext();
    DataFormat df;
    try {
      DataFormatDefinition dfd = camelContext.getDataFormats().get(dataFormatName);
      df = dfd.getDataFormat(new DefaultRouteContext(camelContext));
    } catch (Exception e) {
      throw new IllegalArgumentException("dataformat for [" + dataFormatName + "] is not found.");
    }
    if (df == null) {
      throw new IllegalArgumentException("dataformat for [" + dataFormatName + "] is null.");
    }

    // add dataFormat as service which will also start the service
    // (true => let camelContext handle the lifecycle of the dataFormat)
    camelContext.addService(df, true, true);

    // if stream caching is enabled then use that so we can stream accordingly
    // for example to overflow to disk for big streams
    OutputStreamBuilder osb = OutputStreamBuilder.withExchange(exchange);
    try {
      df.marshal(exchange, exchange.getIn().getBody(), osb);
      exchange.getIn().setBody(osb.build());
    } catch (Exception e) {
      throw new CamelExchangeException("failed to marshal exchange using " + df.toString(), exchange, e);
    }

  }

}