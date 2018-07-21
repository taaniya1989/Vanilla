package edu.sdsu.tvidhate.vanilla;

import android.net.Uri;

import com.google.android.gms.tasks.Task;

public class Place {

    private String mPlaceImageURL = null;

    public Place(Task<Uri> photos)
    {
        this.mPlaceImageURL = null;
    }

    public Place(String mPlaceImageURL) {
        this.mPlaceImageURL = mPlaceImageURL;
    }

    public String getmPlaceImageURL() {
        return mPlaceImageURL;
    }

    public void setmPlaceImageURL(String mPlaceImageURL) {
        this.mPlaceImageURL = mPlaceImageURL;
    }
}
