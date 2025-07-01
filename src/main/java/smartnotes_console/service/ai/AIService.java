package smartnotes_console.service.ai;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import smartnotes_console.common.Storage;

public class AIService {
	public boolean checkPermission() {
		if (Storage.AI_API_TOKEN == null || Storage.AI_API_URL == null || Storage.AI_API_MODEL == null) {
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
			URL url = new URI(Storage.AI_API_URL).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty ("Authorization", "Bearer " + Storage.AI_API_TOKEN);
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
