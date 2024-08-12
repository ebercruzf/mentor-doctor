package com.olinnova.mentordoctor.dto;

public class ResponseGenericDTO {

    private String status;
    private String message;
    private Data data;
    private Metadata metadata;


    public ResponseGenericDTO() {

    }

    public ResponseGenericDTO(String status, String message, Data data, Metadata metadata) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.metadata = metadata;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "ResponseGenericDTO{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", metadata=" + metadata +
                '}';
    }
}
