package com.olinnova.mentordoctor.util;

import com.olinnova.mentordoctor.dto.Data;
import com.olinnova.mentordoctor.dto.GetDateActual;
import com.olinnova.mentordoctor.dto.Metadata;

public class DataMetadataFactory {

    public static Data createData(String id, String response) {

        Data data = new Data();
        data.setId(id);
        data.setResponse(response);
        return data;
    }

    public static Metadata createMetadata() {
        Metadata metadata = new Metadata();
        metadata.setFecha(GetDateActual.getDateActual());
        metadata.setHora(GetDateActual.getTimeActual());
        return metadata;
    }

}
