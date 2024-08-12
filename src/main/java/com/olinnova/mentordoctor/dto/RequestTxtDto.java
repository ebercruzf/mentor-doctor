package com.olinnova.mentordoctor.dto;

public class RequestTxtDto {
    private String format;
    private String sampleRate;
    private String txtData;

    public  RequestTxtDto() {

    }

    public RequestTxtDto(String format, String sampleRate, String txtData) {
        this.format = format;
        this.sampleRate = sampleRate;
        this.txtData = txtData;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(String sampleRate) {
        this.sampleRate = sampleRate;
    }

    public String getTxtData() {
        return txtData;
    }

    public void setTxtData(String txtData) {
        this.txtData = txtData;
    }


    @Override
    public String toString() {
        return "RequestTxtDto{" +
                "format='" + format + '\'' +
                ", sampleRate='" + sampleRate + '\'' +
                ", txtData='" + txtData + '\'' +
                '}';
    }
}
