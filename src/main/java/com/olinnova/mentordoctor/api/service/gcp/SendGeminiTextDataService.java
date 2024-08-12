package com.olinnova.mentordoctor.api.service.gcp;

import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

public interface SendGeminiTextDataService {

    public GenerateContentResponse questionTextSynchronous(String args) throws IOException;

    public ResponseStream<GenerateContentResponse> questionTextAsynchronous(String args) throws IOException;

    public String questionTextAsynchronousTemp(String args) throws IOException;

        //  public Flux<GenerateContentResponse> questionTextAsynchronousFlux(String args) throws IOException;

    public Mono<ResponseStream<GenerateContentResponse>> generateContent(String args);

    }
