package com.tondi.airinfoserver.model;

import java.io.Serializable;

import com.tondi.airinfoserver.model.status.CurrentStatusModel;

public class ResponseModel implements Serializable {

	/**
	 * TODO what is this??
	 */
	private static final long serialVersionUID = 1L;

	private CurrentStatusModel currentStatus;
	
	public void setCurrentStatusModel(CurrentStatusModel status) {
		this.currentStatus = status;
	}
}
