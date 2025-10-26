package com.be08.smart_notes.service.ai;

import com.be08.smart_notes.dto.ai.AIInferenceRequest;
import com.be08.smart_notes.dto.ai.AIInferenceResponse;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AIService {
    @Autowired
    private RestClient restClient;

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
        AIInferenceRequest info = AIInferenceRequest.builder()
                .model(AI_API_MODEL)
                .temperature(AI_API_TEMPERATURE)
                .topP(AI_API_TOP_P)
                .guidedJson(guidedSchema)
                .messages(new AIInferenceRequest.RequestMessage[] {
                        new AIInferenceRequest.RequestMessage(AI_API_SYSTEM_ROLE, systemPrompt),
                        new AIInferenceRequest.RequestMessage(AI_API_USER_ROLE, noteContent)
                })
                .build();

        // Fetch and return response from AI API
        AIInferenceResponse inferenceResponse = fetchResponseFromInferenceProvider(info);
        return inferenceResponse.getChoices()[0].getMessage().getContent();
    }

    private boolean checkPermission() {
        if (AI_API_MODEL == null) {
            System.out.println("Missing AI_API_MODEL.");
            System.out.println("Please check your .env file and try again.");
            return false;
        }
        return true;
    }

	private AIInferenceResponse fetchResponseFromInferenceProvider(AIInferenceRequest info) {
        AIInferenceResponse response = restClient.post()
                .body(info)
                .retrieve()
                .body(AIInferenceResponse.class);
        return response;
	}

    // Unused methods, will debug later
    private String generateGuidedSchema(Class<?> ClassType) {
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);

        JakartaValidationModule validationModule = new JakartaValidationModule(
                JakartaValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED,
                JakartaValidationOption.INCLUDE_PATTERN_EXPRESSIONS
        );
        configBuilder.with(new JacksonModule());
        configBuilder.with(validationModule);

        SchemaGeneratorConfig config = configBuilder.build();
        SchemaGenerator generator = new SchemaGenerator(config);
        ObjectNode jsonSchema = generator.generateSchema(ClassType);
        jsonSchema.remove("$schema");

        return jsonSchema.toString();
    }
}