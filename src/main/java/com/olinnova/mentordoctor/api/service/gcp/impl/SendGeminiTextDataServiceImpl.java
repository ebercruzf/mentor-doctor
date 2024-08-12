package com.olinnova.mentordoctor.api.service.gcp.impl;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import com.olinnova.mentordoctor.api.service.gcp.SendGeminiTextDataService;
import com.olinnova.mentordoctor.util.Common;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.Part;
import reactor.core.scheduler.Schedulers;

@Service
public class SendGeminiTextDataServiceImpl  implements SendGeminiTextDataService{

    private final Environment environment;

    public SendGeminiTextDataServiceImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public GenerateContentResponse questionTextSynchronous(String args) throws IOException {


        try (VertexAI vertexAi = new VertexAI(environment.getProperty(Common.GCP_PROJECT_ID), environment.getProperty(Common.GCP_LOCATION));) {
            GenerativeModel model = new GenerativeModel(environment.getProperty(Common.GCP_GENERATIVE_MODEL), vertexAi);

            GenerateContentResponse response = model.generateContent(args);
            System.out.println("ExampleNormal response: " + response);

            String textResponse = response.getCandidates(0).getContent().getParts(0).getText();
            System.out.println("ExampleReactiveResponse_textResponse: " + textResponse.toString());


            return response;
        }
    }

    @Override
    public ResponseStream<GenerateContentResponse> questionTextAsynchronous(String args) throws IOException {

        long startTime = System.currentTimeMillis();

        try (VertexAI vertexAi = new VertexAI(environment.getProperty(Common.GCP_PROJECT_ID), environment.getProperty(Common.GCP_LOCATION));) {
            GenerativeModel model = new GenerativeModel(environment.getProperty(Common.GCP_GENERATIVE_MODEL), vertexAi);

            ResponseStream<GenerateContentResponse> responseStream = model.generateContentStream(args);

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
//
//            String completeText = fullResponse.toString();
//            System.out.println("AAAAA#####: "+ completeText);

            long endTime = System.currentTimeMillis();  // Capture end time

            long responseTime = endTime - startTime;  // Calculate response time

            System.out.println("SendGeminiTextDataServiceImpl/questionTextAsynchronous Response time (ms): " + responseTime);


            return responseStream;
        }
    }



    @Override
    public String questionTextAsynchronousTemp(String args) throws IOException {

        long startTime = System.currentTimeMillis();

        try (VertexAI vertexAi = new VertexAI(environment.getProperty(Common.GCP_PROJECT_ID), environment.getProperty(Common.GCP_LOCATION));) {
            GenerativeModel model = new GenerativeModel(environment.getProperty(Common.GCP_GENERATIVE_MODEL), vertexAi);

            ResponseStream<GenerateContentResponse> responseStream = model.generateContentStream(args);

            String textTemp = processGeminiText(responseStream);


            long endTime = System.currentTimeMillis();  // Capture end time

            long responseTime = endTime - startTime;  // Calculate response time

            System.out.println("SendGeminiTextDataServiceImpl/questionTextAsynchronousTemp Response time (ms): " + responseTime);


            return textTemp;
        }
    }






    @Override
    public Mono<ResponseStream<GenerateContentResponse>> generateContent(String args) {

        ResponseStream<GenerateContentResponse> responseStream = null;

        try (VertexAI vertexAi = new VertexAI(environment.getProperty(Common.GCP_PROJECT_ID), environment.getProperty(Common.GCP_LOCATION));) {
            GenerativeModel model = new GenerativeModel(environment.getProperty(Common.GCP_GENERATIVE_MODEL), vertexAi);

            responseStream = model.generateContentStream(args);
            System.out.println("ExampleReactiveResponse responseDos: " + responseStream);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Mono.just(responseStream);
    }



    public String processGeminiText(ResponseStream<GenerateContentResponse> result) {
        CompletableFuture<String> responseFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Flux<GenerateContentResponse> responseFlux = Flux.fromIterable(result);

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
