package com.be08.smart_notes.dto.ai;

public class InferenceRequest {
	public String model;
	public InferenceRequestMessage[] messages;
	
	public InferenceRequest(String model, InferenceRequestMessage[] messages) {
		super();
		this.model = model;
		this.messages = messages;
	}
}
