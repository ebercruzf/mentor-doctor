package com.olinnova.mentordoctor.dto;

public class ResponseGenericDTOBuilder {

    private ResponseGenericDTO responseGenericDTO;

    public ResponseGenericDTOBuilder() {
        responseGenericDTO = new ResponseGenericDTO();
    }

    public ResponseGenericDTOBuilder setStatus(String status) {
        responseGenericDTO.setStatus(status);
        return this;
    }

    public ResponseGenericDTOBuilder setMessage(String message) {
        responseGenericDTO.setMessage(message);
        return this;
    }

    public ResponseGenericDTOBuilder setData(Data data) {
        responseGenericDTO.setData(data);
        return this;
    }

    public ResponseGenericDTOBuilder setMetadata(Metadata metadata) {
        responseGenericDTO.setMetadata(metadata);
        return this;
    }

    public ResponseGenericDTO build() {
        return responseGenericDTO;
    }
}
