/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.Serializable;

/**
 *
 * @author creepergd
 */
public class Message implements Serializable {
	private int messageID;
	private String messageContents;
	private int toUserID;
	private int fromUserID;
	
	public Message() {}

	public Message(int messageID, String messageContents, int toUserID, int fromUserID) {
		this.messageID = messageID;
		this.messageContents = messageContents;
		this.toUserID = toUserID;
		this.fromUserID = fromUserID;
	}

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	public String getMessageContents() {
		return messageContents;
	}

	public void setMessageContents(String messageContents) {
		this.messageContents = messageContents;
	}

	public int getToUserID() {
		return toUserID;
	}

	public void setToUserID(int toUserID) {
		this.toUserID = toUserID;
	}

	public int getFromUserID() {
		return fromUserID;
	}

	public void setFromUserID(int fromUserID) {
		this.fromUserID = fromUserID;
	}
}
