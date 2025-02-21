package com.ALGXeon.lingxiquiz.tools;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenAIApiUtils {

    private static final Logger log = LoggerFactory.getLogger(OpenAIApiUtils.class);

    /**
     * Attempts to parse a string containing an AST representation into a JSON object.
     */
    public static Tuple<String, JSONObject> tryParseAstToJson(String functionString) {
        StringBuilder astInfo = new StringBuilder();
        JSONObject jsonResult = new JSONObject();

        // For simplicity, we will assume the input is already in a valid format.
        // In a real scenario, you would need to use a proper AST parser for Java.
        // Here we simulate the process by extracting key-value pairs from the string.
        String pattern = "(\\w+)=(\\{.*?\\})";
        java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = r.matcher(functionString);

        while (m.find()) {
            String argName = m.group(1);
            String argValue = m.group(2);

            astInfo.append("Argument Name: ").append(argName).append("\n");
            astInfo.append("Argument Value: ").append(argValue).append("\n");

            try {
                JSONObject valueJson = new JSONObject(argValue);
                jsonResult.put(argName, valueJson);
            } catch (JSONException e) {
                log.error("Error parsing argument value: {}", argValue, e);
            }
        }

        return new Tuple<>(astInfo.toString(), jsonResult);
    }

    /**
     * Attempts to parse and clean up a potentially malformed JSON string.
     */
    public static Tuple<String, JSONObject> tryParseJsonObject(String input) {
        String cleanedInput = input;
        JSONObject result = null;

        try {
            // Try parsing the input directly first.
            result = new JSONObject(new JSONTokener(cleanedInput));
        } catch (JSONException e) {
            log.info("Warning: Error decoding faulty JSON, attempting repair.");
        }

        if (result != null) {
            return new Tuple<>(cleanedInput, result);
        }

        // Clean up JSON string.
        cleanedInput = cleanJsonString(cleanedInput);

        try {
            result = new JSONObject(new JSONTokener(cleanedInput));
        } catch (JSONException e) {
            log.error("Error loading JSON, json={}", cleanedInput, e);
            return new Tuple<>("", new JSONObject());
        }

        return new Tuple<>(cleanedInput, result);
    }

    /**
     * Cleans up a potentially malformed JSON string.
     */
    public static String cleanJsonString(String input) {
        String cleanedInput = input;

        // Remove any extra characters or patterns.
        cleanedInput = cleanedInput.replaceAll("\\{\\{", "{")
                .replaceAll("\\}\\}", "}")
                .replaceAll("\"\\[\\{", "[{")
                .replaceAll("\\}\\[\"", "}]")
                .replaceAll("\\\\", " ")
                .replaceAll("\\\\n", " ")
                .replaceAll("\n", " ")
                .replaceAll("\r", "")
                .trim();

        // Remove JSON Markdown Frame
        if (cleanedInput.startsWith("```")) {
            cleanedInput = cleanedInput.substring(3);
        }
        if (cleanedInput.startsWith("```json")) {
            cleanedInput = cleanedInput.substring(7);
        }
        if (cleanedInput.endsWith("```")) {
            cleanedInput = cleanedInput.substring(0, cleanedInput.length() - 3);
        }

        return cleanedInput;
    }

    /**
     * Simple Tuple implementation for returning two values.
     */
    public static class Tuple<T1, T2> {
        public final T1 item1;
        public final T2 item2;

        public Tuple(T1 item1, T2 item2) {
            this.item1 = item1;
            this.item2 = item2;
        }
    }

    public static void main(String[] args) {
        // Example usage
        String functionString = "tool_call(first_int={'title': 'First Int', 'type': 'integer'}, second_int={'title': 'Second Int', 'type': 'integer'})";
        Tuple<String, JSONObject> astResult = tryParseAstToJson(functionString);
        System.out.println(astResult.item1);
        System.out.println(astResult.item2.toString());

        String jsonString = "{\"key\": \"value\"}\nSome extra text";
        Tuple<String, JSONObject> jsonResult = tryParseJsonObject(jsonString);
        System.out.println(jsonResult.item1);
        System.out.println(jsonResult.item2.toString());
    }
}