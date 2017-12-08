package com.ibm.mics.entity.base;

import java.util.List;

public class StrMessage extends BaseMessage {
	private List<String> data;
	
	public List<String> getData() {
		return data;
	}
	public void setData(List<String> data) {
		this.data = data;
	}
	
}
