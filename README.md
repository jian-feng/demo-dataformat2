# demo-dataformat2

Demostrate how to use custom Java Processor for Dynamic Marshal/Unmarshal.

- [DynamicMarshal.java](src/main/java/com/redhat/sample/DynamicMarshal.java)
- [DynamicUnmarshal.java](src/main/java/com/redhat/sample/DynamicUnmarshal.java)

Compare to [demo-dataformat][1], now we can have more control of exception handling, and speed up by 30%.

[1]: https://github.com/jian-feng/demo-dataformat

## How to implement

Step 1. Define dataformat bean as many as you neeed
```xml
<bean id="json1" class="org.apache.camel.component.jackson.JacksonDataFormat">
  <property name="unmarshalType" value="com.redhat.sample.Request"/>
</bean>
```

Step 2. Use <process> to call custom Java Processor

```xml
<process ref="dynamicUnmarshal"/>
...
<process ref="dynamicMarshal"/>
``` 

## How to run

[CamelRouteUnitTest][3] is the Junit test case. Just run it and see the result.

[3]: src/test/java/com/redhat/sample/CamelRouteUnitTest.java

```sh
cd demo-dataformat
mvn clean test
```

Sample output of unit test:
```console
CamelRouteUnitTest - response is {"field1":"AAA","field2":"111"}
CamelRouteUnitTest - ********************************************************************************
CamelRouteUnitTest - Testing done: testDynamicMarshal(com.redhat.sample.CamelRouteUnitTest)
CamelRouteUnitTest - Took: 0.277 seconds (277 millis)
CamelRouteUnitTest - ********************************************************************************
CamelRouteUnitTest - response is com.redhat.sample.Request@3ab595c8
CamelRouteUnitTest - ********************************************************************************
CamelRouteUnitTest - Testing done: testDynamicUnmarshal(com.redhat.sample.CamelRouteUnitTest)
CamelRouteUnitTest - Took: 0.012 seconds (12 millis)
CamelRouteUnitTest - ********************************************************************************
CamelRouteUnitTest - Testing done: testPerformace10000(com.redhat.sample.CamelRouteUnitTest)
CamelRouteUnitTest - Took: 1.171 seconds (1171 millis)
CamelRouteUnitTest - ********************************************************************************
CamelRouteUnitTest - Testing done: testPerformace100000(com.redhat.sample.CamelRouteUnitTest)
CamelRouteUnitTest - Took: 3.262 seconds (3262 millis)
CamelRouteUnitTest - ********************************************************************************
```
