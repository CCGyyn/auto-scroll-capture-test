package com.wanjian.scrollscreenshots;

import android.os.Parcel;
import android.os.Parcelable;

public class EventResponse implements  Parcelable{
    //之后先写实现属性再来重新写Parcelable as能自动写好需要的部分
    protected EventResponse(Parcel in) {
    }

    public static final Creator<EventResponse> CREATOR = new Creator<EventResponse>() {
        @Override
        public EventResponse createFromParcel(Parcel in) {
            return new EventResponse(in);
        }

        @Override
        public EventResponse[] newArray(int size) {
            return new EventResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
