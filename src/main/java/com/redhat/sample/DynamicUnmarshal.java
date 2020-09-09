package com.redhat.sample;

import java.io.InputStream;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultRouteContext;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.spi.DataFormat;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("dynamicUnmarshal")
public class DynamicUnmarshal implements Processor {

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

    Object result;
    try {
      InputStream stream = exchange.getIn().getBody(InputStream.class);
      result = df.unmarshal(exchange, stream);
      exchange.getIn().setBody(result);
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      throw new CamelExchangeException("failed to unmarshal exchange using " + df.toString(), exchange, e);
    }

  }

}