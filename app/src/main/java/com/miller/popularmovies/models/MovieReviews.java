
package com.miller.popularmovies.models;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieReviews extends Pagination
{
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("results")
    @Expose
    private List<Review> results = null;

    public final static Creator<MovieReviews> CREATOR = new Creator<MovieReviews>() {
        @SuppressWarnings({
            "unchecked"
        })
        public MovieReviews createFromParcel(Parcel in) {
            MovieReviews instance = new MovieReviews();
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.results, (Review.class.getClassLoader()));
            return instance;
        }

        public MovieReviews[] newArray(int size) {
            return (new MovieReviews[size]);
        }

    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> reviews) {
        this.results = reviews;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(results);
    }

    public int describeContents() {
        return  0;
    }

}
