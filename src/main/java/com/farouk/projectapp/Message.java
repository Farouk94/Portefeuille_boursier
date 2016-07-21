/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farouk.projectapp;

/**
 * Class 'Message' used to give form of a discussion between manager and
 * employee.
 *
 * @author farou_000 */
public class Message {

    private int userID;
    private String message;
    private String date;
    private String readStatus;

    /**
     * Constructor to fill form of a Message instance.
     *
     * @param userID
     * @param message
     * @param date
     * @param readStatus
     */
    public Message(int userID, String message, String date, String readStatus) {
        this.userID = userID;
        this.message = message;
        this.date = date;
        this.readStatus = readStatus;
    }

    /**
     *
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     *
     * @param userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return readStatus
     */
    public String getReadStatus() {
        return readStatus;
    }

    /**
     *
     * @param readStatus
     */
    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    @Override
    public String toString() {
        return "Date : " + date + "\nMessage : " + message + "\nRead : " + readStatus + "\n----------\n";
    }

}
