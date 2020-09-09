# demo-dataformat2

Demostrate how to use custom Java Processor for Dynamic Marshal/Unmarshal.

- [DynamicMarshal.java](src/main/java/com/redhat/sample/DynamicMarshal.java)
- [DynamicUnmarshal.java](src/main/java/com/redhat/sample/DynamicUnmarshal.java)

Compare to [demo-dataformat][1], 
1. now we can have more control of exception handling
2. speed up by 25%
3. dataformat definition can be reused by `<marshal>` and `<unmarshal>`

[1]: https://github.com/jian-feng/demo-dataformat

## How to implement

Step 1. Define dataformat definition as many as you neeed
```xml
<dataFormats>
    <json id="json1" library="Jackson" unmarshalTypeName="com.redhat.sample.Request"/>
</dataFormats>
```

Step 2. Use `<process>` to call custom Java Processor

```xml
<process ref="dynamicUnmarshal"/>
...
<process ref="dynamicMarshal"/>
``` 

## How to run

[CamelRouteUnitTest][3] is the Junit test case. Just run it and see the result.

[3]: src/test/java/com/redhat/sample/CamelRouteUnitTest.java

```sh
cd demo-dataformat2
mvn clean test
```

Sample output of unit test:
```console
com.redhat.sample.CamelRouteUnitTest - response is {"field1":"AAA","field2":"111"}
com.redhat.sample.CamelRouteUnitTest - ********************************************************************************
com.redhat.sample.CamelRouteUnitTest - Testing done: testDynamicMarshal(com.redhat.sample.CamelRouteUnitTest)
com.redhat.sample.CamelRouteUnitTest - Took: 0.389 seconds (389 millis)
com.redhat.sample.CamelRouteUnitTest - ********************************************************************************
com.redhat.sample.CamelRouteUnitTest - response is com.redhat.sample.Request@79ca7bea
com.redhat.sample.CamelRouteUnitTest - ********************************************************************************
com.redhat.sample.CamelRouteUnitTest - Testing done: testDynamicUnmarshal(com.redhat.sample.CamelRouteUnitTest)
com.redhat.sample.CamelRouteUnitTest - Took: 0.013 seconds (13 millis)
com.redhat.sample.CamelRouteUnitTest - ********************************************************************************
com.redhat.sample.CamelRouteUnitTest - Testing done: testPerformace10000(com.redhat.sample.CamelRouteUnitTest)
com.redhat.sample.CamelRouteUnitTest - Took: 1.184 seconds (1184 millis)
com.redhat.sample.CamelRouteUnitTest - ********************************************************************************
com.redhat.sample.CamelRouteUnitTest - Testing done: testPerformace100000(com.redhat.sample.CamelRouteUnitTest)
com.redhat.sample.CamelRouteUnitTest - Took: 3.337 seconds (3337 millis)
com.redhat.sample.CamelRouteUnitTest - ********************************************************************************
```
