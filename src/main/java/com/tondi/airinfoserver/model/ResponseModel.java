package com.tondi.airinfoserver.model;

import java.io.Serializable;

import com.tondi.airinfoserver.model.status.StatusModel;

public class ResponseModel implements Serializable {

	/**
	 * TODO what is this??
	 */
	private static final long serialVersionUID = 1L;

	private StatusModel currentStatus;
	
	public void setCurrentStatusModel(StatusModel status) {
		this.currentStatus = status;
	}
}
