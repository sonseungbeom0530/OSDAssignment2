package it.feio.android.omninotes.models;

import android.os.Parcel;
import android.os.Parcelable;

public class BottomBoxColorData implements Parcelable {

    private String noteID;
    private String color;
    public BottomBoxColorData() {

    }
    public BottomBoxColorData(String noteID) {
        this.noteID = noteID;
    }

    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BottomBoxColorData(Parcel in) {
        noteID = in.readString();
        color = in.readString();
    }

    public static final Creator<BottomBoxColorData> CREATOR = new Creator<BottomBoxColorData>() {
        @Override
        public BottomBoxColorData createFromParcel(Parcel in) {
            return new BottomBoxColorData(in);
        }

        @Override
        public BottomBoxColorData[] newArray(int size) {
            return new BottomBoxColorData[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(noteID);
        parcel.writeString(color);
    }
}
