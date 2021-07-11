package com.imageApp.response;

import java.io.Serializable;
import java.util.List;

import com.imageApp.model.Image;

public class Response implements Serializable {

	private boolean status;
	private List<Image> data;
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public List getData() {
		return data;
	}
	public void setData(List data) {
		this.data = data;
	}
	
	
}
