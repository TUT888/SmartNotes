package smartnotes_console;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import smartnotes_console.common.Storage;
import smartnotes_console.dto.Note;
import smartnotes_console.dto.Quiz;
import smartnotes_console.dto.ai_api_response.QuizResponse;
import smartnotes_console.service.NoteService;
import smartnotes_console.service.QuizService;
import smartnotes_console.service.ai.QuizGenerationService;

public class Main {
	static Scanner sc = new Scanner(System.in);
	
	private static final int OPT_EXIT = 0;
	private static final int OPT_NOTE_LIST = 1;
	private static final int OPT_GENERATE_QUIZ = 2;
	private static final int OPT_GENERATE_SAMPLE_QUIZ = 3;
	
	private static QuizGenerationService quizGenerationService = new QuizGenerationService();
	private static NoteService noteService = new NoteService();
	private static QuizService quizService = new QuizService();
	
	public static void main(String[] args) throws URISyntaxException, IOException {
		if (!loadEnv()) return; 

		while (displayOption());
	}
	
	private static boolean displayOption() {
		// Display option
		System.out.println("\nWELCOME TO SMART NOTES");
		System.out.println(" ________________________________________________");
		System.out.println("| 0. Quit                                        |");
		System.out.println("| 1. View my note                                |");
		System.out.println("| 2. Generate quiz from note (require API TOKEN) |");
		System.out.println("| 3. Try sample quiz from sample response        |");
		System.out.println("|________________________________________________|");
		
		// Get user input
		System.out.print("Enter your option: ");
		int opt = getUserNumberInput();
		if (opt < 0) return true;
		
		Note selectedNote;
		switch (opt) {
			case OPT_EXIT:
				return false;
			case OPT_NOTE_LIST:
				selectedNote = selectNote();
				if (selectedNote == null) {
					System.out.println("There isn't any note in database.");
					return true;
				}
				
				String noteContent = noteService.getNoteContent(selectedNote);
				System.out.println("\n------ Note content ------\n");
				System.out.println(noteContent);
				System.out.println("\n-------- End note --------\n");
				break;
			case OPT_GENERATE_QUIZ:
				selectedNote = selectNote();
				if (selectedNote == null) {
					System.out.println("There isn't any note in database.");
					return true;
				}
				
				System.out.println("Generating quizzes... please wait...");
				QuizResponse quizResponse = quizGenerationService.generateQuizFromNote(selectedNote);
				if (quizResponse == null) {
					System.out.println("There is an error when processing quizzes.");
					return true;
				}
				if (quizResponse.quizzes == null || quizResponse.quizzes.isEmpty()) {
					System.out.println("There is an error when loading data, quizzes not found.");
					return true;
				}
				System.out.println("Done.");
				
				System.out.println("\n------ Quizzes start ------");
				int result = doQuizzes(quizResponse.quizzes);
				System.out.println("\n------- Quizzes end -------");
				System.out.println("Your result: " + result + " / " + quizResponse.quizzes.size());
				break;
			case OPT_GENERATE_SAMPLE_QUIZ:
				QuizResponse sampleQuizResponse = quizGenerationService.generateSampleQuiz();
				if (sampleQuizResponse.quizzes.isEmpty()) {
					System.out.println("There is an error when processing raw quizzes.");
					return true;
				}
				System.out.println("\n------ Quizzes start ------");
				int sampleQuizResult = doQuizzes(sampleQuizResponse.quizzes);
				System.out.println("\n------- Quizzes end -------");
				System.out.println("Your result: " + sampleQuizResult + " / " + sampleQuizResponse.quizzes.size());
				break;
			default:
				System.out.println("Invalid option, please try again.");
		}
		return true;
	}
	
	private static int getUserNumberInput() {
		try {
			String input = sc.nextLine();
			int opt = Integer.parseInt(input);
			
			return opt;
		} catch (Exception e) {
			System.out.println("ERROR: Invalid input received, input must be a valid number in valid range.");
		}
		return -1;
	}
	
	private static Note selectNote() {
		// Get all notes
		ArrayList<Note> noteList = noteService.getAllNotes();
		if (noteList.isEmpty()) return null;

		// Print all notes
		System.out.println("Your notes include:");
		for (int i=0; i<noteList.size(); i++) {
			Note note = noteList.get(i);
			System.out.println("\t" + (i+1) + ". " + note.title + " - " + note.storeFilePath);
		}

		// Get user input
		System.out.println("Please select a note in a valid range.");
		int selectedOpt = -1;
		do {
			System.out.print("> Your selection: ");
			selectedOpt = getUserNumberInput();
		} while (selectedOpt <= 0 || selectedOpt > noteList.size());
		
		Note selectedNote = noteList.get(selectedOpt-1);	
		return selectedNote;
	}
	
	private static int doQuizzes(ArrayList<Quiz> quizzes) {
		int n = quizzes.size();
		int totalCorrect = 0;
		
		for (int i=0; i<n; i++) {
			// Print quiz content
			Quiz quiz = quizzes.get(i);
			System.out.println("Question " + (i+1) + "/" + n + ": " + quiz.question);
			for (int opt=0; opt<quiz.options.length; opt++) {
				System.out.println("\t" + (opt+1) + ". " + quiz.options[opt]);
			}
			
			// Get user input
			System.out.println("Provide your answer from 1-4, or type 0 to exit the quiz.");
			int selectedOpt = -1;
			do {
				System.out.print("> Your selection (0-4): ");
				selectedOpt = getUserNumberInput();
			} while (selectedOpt < 0 || selectedOpt > 4);
			
			if (selectedOpt == 0) return totalCorrect;
			
			// Check user input
			if ((selectedOpt - 1) == quiz.correctIndex) {
				totalCorrect += 1;
				System.out.println("Your answer is correct!");
			} else {
				System.out.println("Your answer is incorrect! Correct answer should be " + (quiz.correctIndex + 1));
			}
			System.out.println();
		}
		return totalCorrect;
	}

	private static boolean loadEnv() {
		Properties props = new Properties();
		
		try {
			Path envFile = Paths.get(".env");
			InputStream inputStream = Files.newInputStream(envFile);
			props.load(inputStream);
		} catch (Exception e) {
			System.out.println("An error occurred when reading .env file");
			System.out.println("Please check your .env file and try again.");
			return false;
		}
		
		Storage.AI_API_TOKEN = (String) props.get("API_TOKEN");
		Storage.AI_API_URL = (String) props.get("API_URL");
		Storage.AI_API_MODEL = (String) props.get("MODEL");
		
		return true;
	}
}
