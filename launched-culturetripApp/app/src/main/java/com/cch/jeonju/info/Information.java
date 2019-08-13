package com.cch.jeonju.info;

import android.graphics.Bitmap;

public class Information {
    int num;
    String dataTitle;
    String designateDate;
    String designateNo;
    String posx;
    String posy;
    String dataSid;
    String dataContent;
    String addrDtl;
    String IntroDataContent;
    String touretcetera;
    String tel;
    String homepage;
    String zipcode;
    String IntroContent;
    String fileUrl;
    Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Information(int num, String dataTitle, String designateDate, String designateNo, String posx, String posy, String dataSid, String dataContent,
                       String addrDtl, String introDataContent, String touretcetera, String tel, String homepage, String zipcode, String introContent) {
        this.num = num;
        this.dataTitle = dataTitle;
        this.designateDate = designateDate;
        this.designateNo = designateNo;
        this.posx = posx;
        this.posy = posy;
        this.dataSid = dataSid;
        this.dataContent = dataContent;
        this.addrDtl = addrDtl;
        this.IntroDataContent = introDataContent;
        this.touretcetera = touretcetera;
        this.tel = tel;
        this.homepage = homepage;
        this.zipcode = zipcode;
        this.IntroContent = introContent;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public String getDesignateDate() {
        return designateDate;
    }

    public void setDesignateDate(String designateDate) {
        this.designateDate = designateDate;
    }

    public String getDesignateNo() {
        return designateNo;
    }

    public void setDesignateNo(String designateNo) {
        this.designateNo = designateNo;
    }

    public String getPosx() {
        return posx;
    }

    public void setPosx(String posx) {
        this.posx = posx;
    }

    public String getPosy() {
        return posy;
    }

    public void setPosy(String posy) {
        this.posy = posy;
    }

    public String getDataSid() {
        return dataSid;
    }

    public void setDataSid(String dataSid) {
        this.dataSid = dataSid;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public String getAddrDtl() {
        return addrDtl;
    }

    public void setAddrDtl(String addrDtl) {
        this.addrDtl = addrDtl;
    }

    public String getIntroDataContent() {
        return IntroDataContent;
    }

    public void setIntroDataContent(String introDataContent) {
        IntroDataContent = introDataContent;
    }

    public String getTouretcetera() {
        return touretcetera;
    }

    public void setTouretcetera(String touretcetera) {
        this.touretcetera = touretcetera;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getIntroContent() {
        return IntroContent;
    }

    public void setIntroContent(String introContent) {
        IntroContent = introContent;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Information{");
        sb.append("dataTitle='").append(dataTitle).append('\'');
        sb.append(", designateDate='").append(designateDate).append('\'');
        sb.append(", designateNo='").append(designateNo).append('\'');
        sb.append(", posx='").append(posx).append('\'');
        sb.append(", posy='").append(posy).append('\'');
        sb.append(", dataSid='").append(dataSid).append('\'');
        sb.append(", dataContent='").append(dataContent).append('\'');
        sb.append(", addrDtl='").append(addrDtl).append('\'');
        sb.append(", IntroDataContent='").append(IntroDataContent).append('\'');
        sb.append(", touretcetera='").append(touretcetera).append('\'');
        sb.append(", tel='").append(tel).append('\'');
        sb.append(", homepage='").append(homepage).append('\'');
        sb.append(", zipcode='").append(zipcode).append('\'');
        sb.append(", IntroContent='").append(IntroContent).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

