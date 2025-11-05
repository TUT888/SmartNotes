package com.be08.smart_notes.service.ai;

import com.be08.smart_notes.config.AIInferenceProperties;
import com.be08.smart_notes.dto.ai.AIInferenceRequest;
import com.be08.smart_notes.dto.ai.AIInferenceResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AIService {
    AIInferenceProperties properties;
    RestClient restClient;

    public AIService(AIInferenceProperties properties, RestClient restClient) {
        this.properties = properties;
        this.restClient = restClient;
    }

    public String generateContent(String systemPrompt, String noteContent, String guidedSchema) {
        if (properties.isMissingCredentials() || properties.isMissingModelConfig()) {
            log.error("AI Inference Configuration is missing or invalid. Please check your .env file and try again");
            throw new AppException(ErrorCode.FAILED_INFERENCE_REQUEST);
        }

        // Create inference JSON body
        AIInferenceRequest info = AIInferenceRequest.builder()
                .model(properties.getModel())
                .temperature(properties.getTemperature())
                .topP(properties.getTopP())
                .guidedJson(guidedSchema)
                .messages(new AIInferenceRequest.RequestMessage[] {
                        new AIInferenceRequest.RequestMessage(properties.getSystemRole(), systemPrompt),
                        new AIInferenceRequest.RequestMessage(properties.getUserRole(), noteContent)
                })
                .build();

        // Fetch and return response from AI API
        AIInferenceResponse inferenceResponse = fetchResponseFromInferenceProvider(info);
        return inferenceResponse.getChoices()[0].getMessage().getContent();
    }

    private AIInferenceResponse fetchResponseFromInferenceProvider(AIInferenceRequest info) {
        AIInferenceResponse response = restClient.post()
                .body(info)
                .retrieve()
                .body(AIInferenceResponse.class);
        return response;
    }

    // Unused methods, will debug later
//    private String generateGuidedSchema(Class<?> ClassType) {
//        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
//
//        JakartaValidationModule validationModule = new JakartaValidationModule(
//                JakartaValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED,
//                JakartaValidationOption.INCLUDE_PATTERN_EXPRESSIONS
//        );
//        configBuilder.with(new JacksonModule());
//        configBuilder.with(validationModule);
//
//        SchemaGeneratorConfig config = configBuilder.build();
//        SchemaGenerator generator = new SchemaGenerator(config);
//        ObjectNode jsonSchema = generator.generateSchema(ClassType);
//        jsonSchema.remove("$schema");
//
//        return jsonSchema.toString();
//    }
}