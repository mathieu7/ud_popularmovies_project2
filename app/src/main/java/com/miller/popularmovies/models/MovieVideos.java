
package com.miller.popularmovies.models;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieVideos implements Parcelable
{
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Video> results = null;

    public final static Creator<MovieVideos> CREATOR = new Creator<MovieVideos>() {
        @SuppressWarnings({
            "unchecked"
        })
        public MovieVideos createFromParcel(Parcel in) {
            MovieVideos instance = new MovieVideos();
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.results, (Video.class.getClassLoader()));
            return instance;
        }

        public MovieVideos[] newArray(int size) {
            return (new MovieVideos[size]);
        }

    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(results);
    }

    public int describeContents() {
        return  0;
    }

}
