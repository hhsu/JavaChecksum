package com.roam.util.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Created by barry on 2017/7/19.
 */
public class CheckSumBinModel {

	private String name;
	private String Chk;

	public CheckSumBinModel() {

	}

	public CheckSumBinModel(String name, String chk) {
		this.name = name;
		Chk = chk;
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
}
