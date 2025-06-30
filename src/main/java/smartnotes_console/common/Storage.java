package smartnotes_console.common;

public class Storage {
	public static final String SOURCE_PATH = "src/main/java/smartnotes_console";

	// Database
	public static final String DB_PATH = SOURCE_PATH + "/db";
	
	// Template & sample text
	public static final String PROMPT_TEMPLATE_PATH = SOURCE_PATH + "/common/text/prompt.txt";
	public static final String SAMPLE_JSON_RESPONSE_PATH = SOURCE_PATH + "/common/text/sample_response.json";
	
	// AI API config
	public static String AI_API_ROLE = "user";
	public static String AI_API_TOKEN;
	public static String AI_API_URL;
	public static String AI_API_MODEL;
}
