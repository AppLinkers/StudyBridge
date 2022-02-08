package com.example.studybridge.domain;

import com.google.gson.annotations.SerializedName;

public class CertificationResponse {

    public CertificationResponse(){

    }

    @SerializedName("imp_uid")
    String impUid;

    @SerializedName("merchant_uid")
    String merchantUid;

    @SerializedName("pg_tid")
    String pgTid;

    @SerializedName("pg_provider")
    String pgProvider;

    @SerializedName("name")
    String name;

    @SerializedName("gender")
    String gender;

    @SerializedName("birth")
    String birth;

    @SerializedName("foreigner")
    boolean foreigner;

    @SerializedName("certified")
    boolean certified;

    @SerializedName("certified_at")
    int certifiedAt;

    @SerializedName("unique_key")
    String uniqueKey;

    @SerializedName("unique_in_site")
    String uniqueInSite;

    @SerializedName("origin")
    String origin;

    public String getImpUid() {
        return impUid;
    }

    public void setImpUid(String impUid) {
        this.impUid = impUid;
    }

    public String getMerchantUid() {
        return merchantUid;
    }

    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }

    public String getPgTid() {
        return pgTid;
    }

    public void setPgTid(String pgTid) {
        this.pgTid = pgTid;
    }

    public String getPgProvider() {
        return pgProvider;
    }

    public void setPgProvider(String pgProvider) {
        this.pgProvider = pgProvider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public boolean isForeigner() {
        return foreigner;
    }

    public void setForeigner(boolean foreigner) {
        this.foreigner = foreigner;
    }

    public boolean isCertified() {
        return certified;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    public int getCertifiedAt() {
        return certifiedAt;
    }

    public void setCertifiedAt(int certifiedAt) {
        this.certifiedAt = certifiedAt;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getUniqueInSite() {
        return uniqueInSite;
    }

    public void setUniqueInSite(String uniqueInSite) {
        this.uniqueInSite = uniqueInSite;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
