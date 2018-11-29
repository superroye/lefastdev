package com.zuzuche.demo.fastdev.component.network.bean;

import java.io.Serializable;

/**
 * @author Roye
 * @date 2018/6/30
 */
public class City implements Serializable {


    public City() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isEasy() {
        return easy;
    }

    public void setEasy(boolean easy) {
        this.easy = easy;
    }

    public String getCity_cn() {
        return city_cn;
    }

    public void setCity_cn(String city_cn) {
        this.city_cn = city_cn;
    }

    public String getCity_en() {
        return city_en;
    }

    public void setCity_en(String city_en) {
        this.city_en = city_en;
    }

    public String getRegion_cn() {
        return region_cn;
    }

    public void setRegion_cn(String region_cn) {
        this.region_cn = region_cn;
    }

    public String getRegion_en() {
        return region_en;
    }

    public void setRegion_en(String region_en) {
        this.region_en = region_en;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLandmark_id() {
        return landmark_id;
    }

    public void setLandmark_id(String landmark_id) {
        this.landmark_id = landmark_id;
    }

    public String getLandmark_en() {
        return landmark_en;
    }

    public void setLandmark_en(String landmark_en) {
        this.landmark_en = landmark_en;
    }

    public String getLandmark_cn() {
        return landmark_cn;
    }

    public void setLandmark_cn(String landmark_cn) {
        this.landmark_cn = landmark_cn;
    }

    private String city;
    private boolean easy;
    private String city_cn;
    private String city_en;
    private String region;
    private String region_cn;
    private String region_en;
    private String state;
    private String letter;
    private String landmark_id;
    private String landmark_en;
    private String landmark_cn;
    private String pinyin;

}
