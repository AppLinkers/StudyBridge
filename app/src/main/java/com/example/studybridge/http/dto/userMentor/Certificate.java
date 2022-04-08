package com.example.studybridge.http.dto.userMentor;

import java.io.Serializable;

public class Certificate implements Serializable {

    private String certificate;

    private String imgUrl;

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
}
