package com.example.wubingzhang.week9.note;

/**
 * Created by shibajyotidebbarma on 07/03/16.
 */
public class DateTimeSorter {
    public int mIndex;
    public String mDateTime;


    public DateTimeSorter(int index, String DateTime){
        mIndex = index;
        mDateTime = DateTime;
    }

    public DateTimeSorter(){}


    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String dateTime) {
        mDateTime = dateTime;
    }
}
