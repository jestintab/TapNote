package com.coltan.tapnote.db;


public class Note {
    //private variables
    int _id;
    String _title;
    String _note;
    String _tag;
    long _date;
    String _starred;

    // Empty constructor
    public Note() {

    }

    // constructor
    public Note(String title, String note, String tag, long date, String starred) {
        this._title = title;
        this._note = note;
        this._tag = tag;
        this._date = date;
        this._starred = starred;
    }

    public Note(int id, String title, String note, String tag, long date) {
        this._id = id;
        this._title = title;
        this._note = note;
        this._tag = tag;
        this._date = date;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting ID
    public void setID(int id) {
        this._id = id;
    }

    // getting title
    public String getTitle() {
        return this._title;
    }

    // setting title
    public void setTitle(String title) {
        this._title = title;
    }

    // getting note
    public String getNote() {
        return this._note;
    }

    // setting note
    public void setNote(String note) {
        this._note = note;
    }

    // getting tag
    public String getTag() {
        return this._tag;
    }

    // setting tag
    public void setTag(String tag) {
        this._tag = tag;
    }

    // getting date
    public long getDate() {
        return this._date;
    }

    // setting date
    public void setDate(long date) {
        this._date = date;
    }

    // getting date
    public String getStarred() {
        return this._starred;
    }

    // setting date
    public void setStarred(String starred) {
        this._starred = starred;
    }
}
