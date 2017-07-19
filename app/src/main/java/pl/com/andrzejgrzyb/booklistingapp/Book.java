package pl.com.andrzejgrzyb.booklistingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Andrzej on 19.07.2017.
 */

public class Book implements Parcelable {

    private String title;
    private String[] authors;

    public Book(String title, String[] authors) {
        this.title = title;
        this.authors = authors;
    }

    protected Book(Parcel in) {
        this.title = in.readString();
        this.authors = in.createStringArray();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAuthors() {
        return authors;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeStringArray(this.authors);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}