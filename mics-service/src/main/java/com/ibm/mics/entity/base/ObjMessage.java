package com.ibm.mics.entity.base;

import java.util.List;

public class ObjMessage extends BaseMessage {
	private List<Data> data;

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

}
