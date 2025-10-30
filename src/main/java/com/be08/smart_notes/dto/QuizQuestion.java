package com.be08.smart_notes.dto;

public class QuizQuestion {
	public String question;
	public String[] options;
	public int correctIndex;

	public QuizQuestion() {
		this.question = "";
		this.options = new String[4];
		this.correctIndex = -1;
	}

	public QuizQuestion(String question, String[] options, int correctIndex) {
		this.question = question;
		this.options = options;
		this.correctIndex = correctIndex;
	}
}
