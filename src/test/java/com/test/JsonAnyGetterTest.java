package com.test;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonAnyGetterTest {

    private ObjectMapper objectMapper;
    private Map<String, String> foos;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        objectMapper.setPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL));

        foos = new HashMap<>();
        foos.put("foo1", "foo1value");
        foos.put("foo2", "foo2value");
        foos.put("foo3", null);
    }

    @Test
    public void testNullMapValuesWhenUsingJsonAnyGetter() throws JsonProcessingException {
        ObjectWithAnyGetter objectWithAnyGetter = new ObjectWithAnyGetter();
        objectWithAnyGetter.values = foos;

        final String jsonString = objectMapper.writeValueAsString(objectWithAnyGetter);
        System.out.println(jsonString);
        assertThat(jsonString).doesNotContain("foo3");
    }

    private static class ObjectWithAnyGetter {

        Map<String, String> values;

        @JsonAnyGetter
        Map<String, String> getValues() {
            return values;
        }
    }

    @Test
    public void testNullMapValuesWhenNotUsingJsonAnyGetter() throws JsonProcessingException {
        ObjectWithoutAnyGetter objectWithoutAnyGetter = new ObjectWithoutAnyGetter();
        objectWithoutAnyGetter.values = foos;

        final String jsonString = objectMapper.writeValueAsString(objectWithoutAnyGetter);
        System.out.println(jsonString);
        assertThat(jsonString).doesNotContain("foo3");
    }

    private static class ObjectWithoutAnyGetter {

        public Map<String, String> values;

    }
}
