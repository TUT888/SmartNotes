package com.be08.smart_notes.dto.ai;

public class InferenceResponse {
	public String id;
	public InferenceResponseChoice[] choices;
	public String model;
	public String object;
}
