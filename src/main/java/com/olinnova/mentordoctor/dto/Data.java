package com.olinnova.mentordoctor.dto;

public class Data {
    private String id;
    private String response;

    public Data() {

    }

    public Data(String id, String response) {
        this.id = id;
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


}
