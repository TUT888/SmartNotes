package com.be08.smart_notes.service.ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.be08.smart_notes.dto.ai.AIInferenceRequest;
import com.be08.smart_notes.dto.ai.QuizResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AIService {
	@Value("${API_TOKEN}")
	protected String AI_API_TOKEN;

	@Value("${API_URL}")
	protected String AI_API_URL;

	@Value("${MODEL}")
	protected String AI_API_MODEL;

	@Value("${AI_API_USER_ROLE}")
	protected String AI_API_USER_ROLE;

	@Value("${AI_API_SYSTEM_ROLE}")
	protected String AI_API_SYSTEM_ROLE;

	@Value("${AI_API_TEMPERATURE}")
	protected double AI_API_TEMPERATURE;

	@Value("${AI_API_TOP_P}")
	protected double AI_API_TOP_P;

    public String generateContent(String systemPrompt, String noteContent, String guidedSchema) {
        // Check environment setup
        if (!checkPermission()) {
            return null;
        }

        // Create inference JSON body
        Gson gson = new Gson();
        AIInferenceRequest info = AIInferenceRequest.builder()
                .model(AI_API_MODEL)
                .temperature(AI_API_TEMPERATURE)
                .top_p(AI_API_TOP_P)
                .guided_json(guidedSchema)
                .messages(new AIInferenceRequest.RequestMessage[] {
                        new AIInferenceRequest.RequestMessage(AI_API_SYSTEM_ROLE, systemPrompt),
                        new AIInferenceRequest.RequestMessage(AI_API_USER_ROLE, noteContent)
                })
                .build();
        String chatJSON = gson.toJson(info);

        // Fetch and return response from AI API
        return fetchResponseFromInferenceProvider(chatJSON);
    }

    private boolean checkPermission() {
        if (AI_API_TOKEN == null || AI_API_URL == null || AI_API_MODEL == null) {
            System.out.println("Missing AI_API_TOKEN or AI_API_URL or AI_API_MODEL.");
            System.out.println("Please check your .env file and try again.");
            return false;
        }
        return true;
    }

    private String generateGuidedSchema(Class<?> ClassType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator(mapper);
            JsonSchema schema = schemaGenerator.generateSchema(ClassType);

            String schemaString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema);
            return schemaString;
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return null;
    }

	private String fetchResponseFromInferenceProvider(String JSONBody) {
		StringBuilder response = new StringBuilder();
		try {
			// Send request
			URL url = new URI(AI_API_URL).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", "Bearer " + AI_API_TOKEN);
			connection.setDoOutput(true);

			try (OutputStream os = connection.getOutputStream()) {
				os.write(JSONBody.getBytes(StandardCharsets.UTF_8));
			}

			// Handle response
			int responseCode = connection.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				System.out.println("Error: HTTP Response code - " + responseCode);
				return "";
			}

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
            }

			connection.disconnect();
		} catch (IOException | URISyntaxException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return response.toString();
	}
}