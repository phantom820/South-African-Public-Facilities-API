package com.south.african.schools.api.util.encoding;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    private Json() { }

    /**
     * Marshals a given object into JSON representation.
     * @param thing An object to be converted to JSON .
     * @return JSON representation of the input.
     * @param <T>
     */
    public static <T> JSONObject marshal(final T thing) {
        final String jsonString = GSON.toJson(thing);
        final JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(jsonString);
            return json;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Marshals a given list into JSON format.
     * @param things A list of objects to be converted into their JSON representation.
     * @return A list containing JSON representation of each item from the input list.
     * @param <T>
     */
    public static <T> List<JSONObject> marshal(final Collection<T> things) {
        return things.stream()
                .map(t -> marshal(t))
                .collect(Collectors.toList());
    }
}
