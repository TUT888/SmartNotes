package smartnotes_console.common;

public class Storage {
	public static final String SOURCE_PATH = "src/main/java/smartnotes_console";

	// Database
	public static final String DB_PATH = SOURCE_PATH + "/db";
	
	// Template & sample text
	public static final String SYSTEM_PROMPT_TEMPLATE_PATH = SOURCE_PATH + "/common/text/system_prompt.txt";
	public static final String QUIZ_RESPONSE_SCHEMA_PATH = SOURCE_PATH + "/common/text/quiz_response_schema.json";
	public static final String SAMPLE_JSON_RESPONSE_PATH = SOURCE_PATH + "/common/text/sample_response.json";
	
	// AI API config
	public static String AI_API_USER_ROLE = "user";
	public static String AI_API_SYSTEM_ROLE = "system";
	public static double AI_API_TEMPERATURE = 0.7;
	public static double AI_API_TOP_P = 0.9;
	public static String AI_API_TOKEN;
	public static String AI_API_URL;
	public static String AI_API_MODEL;
}
