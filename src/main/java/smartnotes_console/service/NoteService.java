package smartnotes_console.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import smartnotes_console.common.Storage;
import smartnotes_console.dto.Note;

public class NoteService {
	public ArrayList<Note> getAllNotes() {
		String filepath = Storage.DB_PATH + "/notes.txt";
		
		ArrayList<Note> currentNoteList = new ArrayList<Note>();
		try {
			// Read from text file
			File myObj = new File(filepath);
			Scanner myReader = new Scanner(myObj);
			myReader.nextLine();
			
			// Read row by row
			while (myReader.hasNextLine()) {
				String textRow = myReader.nextLine();
				String[] data = textRow.split(",");

				int id = Integer.parseInt(data[0]);
				String title = data[1];
				String storeFilePath = Storage.DB_PATH + "/notes/" + data[2];
				currentNoteList.add(new Note(id, title, storeFilePath));
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		return currentNoteList;
	}
	
	public String getNoteContent(Note note) {
		String noteContent = "";
		
		try {
			noteContent = Files.readString(Path.of(note.storeFilePath), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		return noteContent;
	}
}
