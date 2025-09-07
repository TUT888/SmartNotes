package com.be08.smart_notes.dto.ai;

public class InferenceRequestMessage {
	public String role;
	public String content;
	
	public InferenceRequestMessage(String role, String content) {
		this.role = role;
		this.content = content;
	}
}
