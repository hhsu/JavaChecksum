package com.roam.util.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Created by barry on 2017/7/19.
 */
public class CheckSumUnsModel {

	private String name;
	private String Chk;
	private List<CheckSumBinModel> details;

	public CheckSumUnsModel() {

	}

	public CheckSumUnsModel(String name, String chk, List<CheckSumBinModel> details) {
		this.name = name;
		Chk = chk;
		this.details = details;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChk() {
		return Chk;
	}

	public void setChk(String chk) {
		Chk = chk;
	}

	public List<CheckSumBinModel> getDetails() {
		return details;
	}

	public void setDetails(List<CheckSumBinModel> details) {
		this.details = details;
	}

	public JsonNode getJsonFormat(CheckSumUnsModel checkSumUnsModel) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		JsonNode wholeObj = null;
		try {
			jsonInString = "{\"SW" + "\":" + mapper.writeValueAsString(checkSumUnsModel) + "}";
			jsonInString = jsonInString.replace("chk", "Chk");
			wholeObj = mapper.readTree(jsonInString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wholeObj;
	}
}
