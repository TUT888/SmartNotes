package com.be08.smart_notes.common;

public class Storage {
	// Template & sample text
	public static final String RESOURCE_PATH = "src/main/resources";
	public static final String SYSTEM_PROMPT_TEMPLATE_PATH = RESOURCE_PATH + "/prompts/system_prompt.txt";
	public static final String QUIZ_RESPONSE_SCHEMA_PATH = RESOURCE_PATH + "/schemas/quiz_response.json";
	public static final String SAMPLE_JSON_RESPONSE_PATH = RESOURCE_PATH + "/schemas/sample_response.json";
	
	// AI API config
	public static String AI_API_USER_ROLE = "user";
	public static String AI_API_SYSTEM_ROLE = "system";
	public static double AI_API_TEMPERATURE = 0.7;
	public static double AI_API_TOP_P = 0.9;
	public static String AI_API_TOKEN;
	public static String AI_API_URL;
	public static String AI_API_MODEL;
}
