package com.olinnova.mentordoctor.dto;

public class  RequestAudioDto {

    private String format;
    private String sampleRate;
    private String audio;

    public  RequestAudioDto() {

    }

    public  RequestAudioDto(String format, String sampleRate, String audio) {
        this.format = format;
        this.sampleRate = sampleRate;
        this.audio = audio;
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

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    @Override
    public String toString() {
        return "ResquestAudioDto{" +
                "format='" + format + '\'' +
                ", sampleRate='" + sampleRate + '\'' +
                ", audio='" + audio + '\'' +
                '}';
    }
}
