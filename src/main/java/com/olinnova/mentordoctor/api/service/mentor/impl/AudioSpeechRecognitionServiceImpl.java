package com.olinnova.mentordoctor.api.service.mentor.impl;


import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import com.google.protobuf.ByteString;
import com.olinnova.mentordoctor.api.service.gcp.SendToGeminiWavToTxtService;
import com.olinnova.mentordoctor.api.service.mentor.AudioSpeechRecognitionService;
import com.olinnova.mentordoctor.api.service.gcp.SendGeminiTextDataService;
import com.olinnova.mentordoctor.api.service.gcp.impl.SendGeminiTextDataServiceImpl;
import com.olinnova.mentordoctor.dto.*;

import com.olinnova.mentordoctor.util.TemplateData;
import com.olinnova.mentordoctor.util.TemplateProcessor;
import com.olinnova.mentordoctor.util.WebmToWavConverter;
import org.bytedeco.ffmpeg.avutil.LogCallback;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import reactor.core.publisher.Flux; // Import the Flux class
import reactor.core.scheduler.Schedulers; // Import Schedulers class (if not already imported)


import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import java.util.concurrent.CompletableFuture;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import java.io.IOException;
import java.time.Duration;
import javax.swing.*;
import java.io.FileOutputStream;
import java.io.File;

import java.util.concurrent.CompletableFuture;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@Service
public class AudioSpeechRecognitionServiceImpl implements AudioSpeechRecognitionService {

    @Autowired
    private SendGeminiTextDataService sendGeminiTextDataService;

    private final SendToGeminiWavToTxtService sendToGeminiWavToTxtService;
    public AudioSpeechRecognitionServiceImpl(SendGeminiTextDataService sendGeminiTextDataService, SendToGeminiWavToTxtService sendToGeminiWavToTxtService) {
        this.sendGeminiTextDataService = sendGeminiTextDataService;
        this.sendToGeminiWavToTxtService = sendToGeminiWavToTxtService;
    }

    @Override
    public Mono<ResponseGenericDTO> audioSpeechRecognitionProcess(RequestAudioDto requestBody) {



        // Datos codificados en Base64
        String base64Data = requestBody.getAudio(); // Reemplaza con tus datos
        byte[] webmBytes = Base64.getDecoder().decode(base64Data);


        String outputFileWebm = "src/main/resources/audio/video.webm";

        File file = new File(outputFileWebm);

// Si el archivo ya existe, se borra
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Archivo existente borrado exitosamente.");
            } else {
                System.err.println("No se pudo borrar el archivo existente.");
            }
        }

        try (FileOutputStream fos = new FileOutputStream(outputFileWebm)) {
            fos.write(webmBytes);
            System.out.println("Archivo WebM generado exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo WebM: " + e.getMessage());
        }

        String outputFileWav = "src/main/resources/audio/output.wav";

        File fileWav = new File(outputFileWav);

        if (fileWav.exists()) {
            if (fileWav.delete()) {
                System.out.println("Archivo existente borrado exitosamente.");
            } else {
                System.err.println("No se pudo borrar el archivo existente.");
            }
        }



        WebmToWavConverter webmToWavConverter = new WebmToWavConverter();
        webmToWavConverter.converter(outputFileWebm, outputFileWav);


        String respuesta = sendToGeminiWavToTxtService.sendToGemini(outputFileWav);

        SendGeminiTextDataServiceImpl sendGeminiTextDataServiceImpl;

        ResponseStream<GenerateContentResponse> responseStream = null;
        String completeText = "";


        TemplateProcessor processor = new TemplateProcessor();
        TemplateData data = new TemplateData();

        data.addValue("tema", respuesta);

        String template = "Dame información para poder comprender el tema acerca de {tema}, explícala de forma clara como si la estuviera explicando Baldor, clasifica la información por tema y crea una lista en texto plano, agrega también un campo que contenga los elementos si existen, de su ficha bibliográfica, especificando en número de hoja donde se estudia el tema, donde se describa y explique el tema. Atributo donde se ponga una introducción. Y agrega un campo para mostrar link o url de internet si existen., clasifica la información por tema y crea una lista en texto, recomienda consultar y muestra fuentes bibliográficas especializadas para un análisis más profundo del tema.";

        String result = processor.processTemplate(template, data);
        System.out.println("PROMPT_FABRICADO: " + result);


        // responseStream = sendGeminiTextDataService.questionTextAsynchronous(result);
        long startTime = System.currentTimeMillis();
       // completeText = processGeminiText(result);

        try {
            completeText = sendGeminiTextDataService.questionTextAsynchronousTemp(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//            StringBuilder fullResponse = new StringBuilder();
//
//            responseStream.forEach(response -> {
//                if (response.getCandidatesCount() > 0) {
//                    Content content = response.getCandidates(0).getContent();
//                    for (int i = 0; i < content.getPartsCount(); i++) {
//                        Part part = content.getParts(i);
//                        if (part.hasText()) {
//                            fullResponse.append(part.getText());
//                        }
//                    }
//                }
//            });
//            completeText = fullResponse.toString();


        long endTime = System.currentTimeMillis();  // Capture end time

        long responseTime = endTime - startTime;  // Calculate response time

        System.out.println("AudioSpeechRecognitionServiceImpl/audioSpeechRecognitionProcess Response time (ms): " + responseTime);


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



    public String processGeminiText(String result) {
        CompletableFuture<String> responseFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Flux<GenerateContentResponse> responseFlux = Flux.fromIterable(sendGeminiTextDataService.questionTextAsynchronous(result));

                return responseFlux
                        .parallel()
                        .runOn(Schedulers.parallel())
                        .flatMap(this::processResponse)
                        .sequential()
                        .reduce(new StringBuilder(), StringBuilder::append)
                        .map(StringBuilder::toString)
                        .block(Duration.ofSeconds(55)); // Tiempo límite total
            } catch (Exception e) {
                throw new RuntimeException("Error processing Gemini text", e);
            }
        });

        try {
            return responseFuture.completeOnTimeout("Tiempo de espera agotado", 58, java.util.concurrent.TimeUnit.SECONDS)
                    .exceptionally(this::handleException)
                    .get();
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }

    private Flux<String> processResponse(GenerateContentResponse response) {
        return Flux.fromIterable(() -> response.getCandidatesList().iterator())
                .flatMap(candidate -> Flux.fromIterable(() -> candidate.getContent().getPartsList().iterator()))
                .filter(Part::hasText)
                .map(Part::getText);
    }

    private String handleException(Throwable ex) {
        if (ex instanceof TimeoutException) {
            return "La respuesta tardó demasiado. Por favor, inténtelo de nuevo.";
        }
        return "Ocurrió un error al procesar la respuesta: " + ex.getMessage();
    }












}
