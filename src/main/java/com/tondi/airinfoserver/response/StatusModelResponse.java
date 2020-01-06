package com.tondi.airinfoserver.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.tondi.airinfoserver.model.status.StatusModel;

public class StatusModelResponse {
	@JsonUnwrapped
	private StatusModel statusModel;

	public StatusModelResponse(StatusModel statusModel) {
		super();
		this.statusModel = statusModel;
	}
	
	public StatusModel getStatusModel() {
		return this.statusModel;
	}
}
