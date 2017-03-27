package com.miller.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieList extends Pagination {

    @SerializedName("results")
    @Expose
    private List<Movie> results = null;

    public final static Parcelable.Creator<MovieList> CREATOR = new Creator<MovieList>() {
        @SuppressWarnings({
                "unchecked"
        })
        public MovieList createFromParcel(Parcel in) {
            MovieList instance = new MovieList();
            in.readList(instance.results, (com.miller.popularmovies.models.Movie.class.getClassLoader()));
            return instance;
        }
        public MovieList[] newArray(int size) {
            return (new MovieList[size]);
        }

    };


    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }
}


