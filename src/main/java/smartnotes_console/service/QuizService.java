package smartnotes_console.service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import smartnotes_console.dto.Quiz;

public class QuizService {
	public ArrayList<Quiz> processQuizFromRawString(String rawQuizzes) {
		ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
		
		String questionRegex = "\\*\\*QUESTION\\*\\*\\s+\\d+:(.+?)\\*\\*END QUESTION\\*\\*\\s+"
				+ "\\*\\*OPTION\\*\\*\\s*(.+?)\\*\\*END OPTION\\*\\*\\s*"
				+ "\\*\\*ANS\\*\\*:(.+?)\\*\\*END ANS\\*\\*";
		
		// Find all quizzes and loop through each quiz group
		Matcher quizMatcher = Pattern.compile(questionRegex, Pattern.DOTALL).matcher(rawQuizzes);
		while (quizMatcher.find()) {
			String foundGroup = quizMatcher.group();
			Quiz newQuiz = new Quiz();
			
			// Find all elements within this group (question, 4 options and correct answer )
			Matcher matcher = Pattern.compile("\\*\\*([A-Z])+\\*\\*\\s*\\d*:(.+?)\\*\\*END ([A-Z])+\\*\\*\\s*").matcher(foundGroup);
			int totalFoundOptions = 0;
			while (matcher.find()) {
				String found = matcher.group();
				switch (found.charAt(2)) {
					case 'Q':
						processQuestionString(newQuiz, found);
						break;
					case 'O':
						totalFoundOptions += 1;
						processOptionString(newQuiz, found);
						break;
					case 'A':
						processAnswerString(newQuiz, found);
						break;
				}
				if (newQuiz.correctIndex != -1) break;
			};
			
			// If the information is not enough to form a quiz, skip this group
			if (!newQuiz.question.isEmpty() && totalFoundOptions==4 && newQuiz.correctIndex!=-1) {
				quizzes.add(newQuiz);
			}
		}
		
		return quizzes;
	}
	
	// ====== Private functions ====== //
	private void processQuestionString(Quiz newQuiz, String text) {
		String startRegex = "\\*\\*([A-Z])+\\*\\*\\s*\\d*:\\s*";
		String endRegex = "\\*\\*END ([A-Z])+\\*\\*\\s*";

		newQuiz.question = text.replaceAll(startRegex, "").replaceAll(endRegex, "");
	}
	
	private void processOptionString(Quiz newQuiz, String text) {
		String startRegex = "\\*\\*([A-Z])+\\*\\*\\s*";
		String endRegex = "\\*\\*END ([A-Z])+\\*\\*\\s*";
		String[] opt = text.replaceAll(startRegex, "").replaceAll(endRegex, "").split(":");
		
		int i = Integer.parseInt(opt[0])-1;
		String content = opt[1].trim();
		
		newQuiz.options[i] = content;
	}
	
	private void processAnswerString(Quiz newQuiz, String text) {
		String startRegex = "\\*\\*([A-Z])+\\*\\*:\\s*";
		String endRegex = "\\*\\*END ([A-Z])+\\*\\*\\s*";
		
		newQuiz.correctIndex = Integer.parseInt(text.replaceAll(startRegex, "").replaceAll(endRegex, "").trim()) - 1;
	}
}
