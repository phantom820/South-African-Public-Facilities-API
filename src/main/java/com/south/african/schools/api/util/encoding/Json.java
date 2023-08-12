package com.south.african.schools.api.util.encoding;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for marshalling objects to JSON.
 */
public final class Json {

    /**
     * Used for marshaling objects to JSON format.
     */
    private static  final ObjectMapper MAPPER = new ObjectMapper();

    private Json() { }

    /**
     * Marshals a given object into JSON representation.
     * @param thing An object to be converted to JSON .
     * @return JSON representation of the input.
     * @param <T>
     */
    public static <T> JSONObject marshal(final T thing) throws JsonProcessingException {
        final String jsonString = MAPPER.writeValueAsString(thing);
        final JSONParser parser = new JSONParser();
        try {
            final JSONObject json = (JSONObject) parser.parse(jsonString);
            return json;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Marshals a given object into JSON string representation.
     * @param thing An object to be converted to JSON .
     * @return JSON string representation of the input.
     * @param <T>
     */
    public static <T> String string(final T thing) {
        try {
            return MAPPER.writeValueAsString(thing);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Marshals a given list into JSON format.
     * @param things A list of objects to be converted into their JSON representation.
     * @return A list containing JSON representation of each item from the input list.
     * @param <T>
     */
    public static <T> List<JSONObject> marshal(final Collection<T> things) {
        return things.stream()
                .map(t -> {
                    try {
                        return marshal(t);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
