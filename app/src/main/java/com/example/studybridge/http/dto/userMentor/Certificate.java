package com.example.studybridge.http.dto.userMentor;

import android.os.Parcel;
import android.os.Parcelable;

public class Certificate implements Parcelable {

    private String certificate;

    private String imgUrl;

    public Certificate(Parcel in) {
        readFromParcel(in);
    }

    public Certificate(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public Certificate(String certificate,String imgUrl) {
        this.certificate = certificate;
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "certificate='" + certificate + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

    public String getCertificate() {
        return certificate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(certificate);
        dest.writeString(imgUrl);
    }
    private void readFromParcel(Parcel in){
        certificate = in.readString();
        imgUrl = in.readString();
    }

    public static final Creator<Certificate> CREATOR = new Creator<Certificate>() {
        @Override
        public Certificate createFromParcel(Parcel in) {
            return new Certificate(in);
        }

        @Override
        public Certificate[] newArray(int size) {
            return new Certificate[size];
        }
    };
}
