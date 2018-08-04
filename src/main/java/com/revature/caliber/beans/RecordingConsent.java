package com.revature.caliber.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RecordingConsent implements Serializable{
	@JsonProperty("Yes")
	Yes,
	@JsonProperty("No")
	No,
	@JsonProperty("Did Not Ask")
	Did_Not_Ask
}
