package com.olinnova.mentordoctor.api.service.mentor.impl;

import com.olinnova.mentordoctor.api.service.gcp.SendGeminiTextDataService;
import com.olinnova.mentordoctor.api.service.mentor.TextSpeechFindService;
import com.olinnova.mentordoctor.dto.*;
import com.olinnova.mentordoctor.util.TemplateData;
import com.olinnova.mentordoctor.util.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;


@Service
public class TextSpeechFindServiceImpl implements TextSpeechFindService {

    @Autowired
    private SendGeminiTextDataService sendGeminiTextDataService;

    @Override
    public Mono<ResponseGenericDTO> textSpeechFindProcess(RequestTxtDto respuesta) {

        String completeText = "";

        TemplateProcessor processor = new TemplateProcessor();
        TemplateData data = new TemplateData();
       // String template = "Dame información para poder comprender el tema acerca de {tema}, explícala de forma clara como si la estuviera explicando Baldor, clasifica la información por tema y crea una lista en texto plano, agrega también un campo que contenga los elementos si existen, de su ficha bibliográfica, especificando en número de hoja donde se estudia el tema, donde se describa y explique el tema. Atributo donde se ponga una introducción. Y agrega un campo para mostrar link o url de internet si existen., clasifica la información por tema y crea una lista en texto, recomienda consultar y muestra fuentes bibliográficas especializadas para un análisis más profundo del tema.";
        String template = "Give me information so I can understand the topic about {tema}, explain it clearly as if Baldor were explaining it, classify the information by topic and create a list in plain text, also add a field that contains the elements if they exist, of its bibliographic record, specifying the page number where the topic is studied, where the topic is described and explained. Attribute where an introduction is placed. And add a field to show a link or internet url if they exist., classify the information by topic and create a list in text, recommend consulting and show specialized bibliographic sources for a deeper analysis of the topic.";
        data.addValue("tema", respuesta.getTxtData().toString());
        String result = processor.processTemplate(template, data);

        long startTime = System.currentTimeMillis();

        try {
            completeText = sendGeminiTextDataService.questionTextAsynchronousTemp(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();  // Capture end time

        long responseTime = endTime - startTime;  // Calculate response time

        System.out.println("TextSpeechFindServiceImpl/textSpeechFindProcess Response time (ms): " + responseTime);


        ResponseGenericDTO responseGenericDTO = new ResponseGenericDTO();

        responseGenericDTO.setStatus("200");
        responseGenericDTO.setMessage("Successful request");
        Data dataFinal = new Data();
        dataFinal.setId("1");
        Metadata metadata = new Metadata();
        metadata.setFecha(GetDateActual.getDateActual());
        metadata.setHora(GetDateActual.getTimeActual());
        dataFinal.setResponse(completeText);
        responseGenericDTO.setData(dataFinal);
        responseGenericDTO.setMetadata(metadata);





        return Mono.just(responseGenericDTO);
    }


}
