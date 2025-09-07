package com.be08.smart_notes.service.ai;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;

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
	
	public boolean checkPermission() {
		if (AI_API_TOKEN == null || AI_API_URL == null || AI_API_MODEL == null) {
			System.out.println("Missing AI_API_TOKEN or AI_API_URL or AI_API_MODEL.");
			System.out.println("Please check your .env file and try again.");
			return false;
		}
		return true;
	}
	
	public String fetchResponseFromInferenceProvider(String JSONBody) {
		String response = "";
		try {
			// Send request
			URL url = new URI(AI_API_URL).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty ("Authorization", "Bearer " + AI_API_TOKEN);
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
			
			try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8)) {
				while (scanner.hasNextLine()) {
					response += scanner.nextLine() + "\n";
				}
			}
			
			connection.disconnect();
		} catch (IOException | URISyntaxException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return response;
	}
}