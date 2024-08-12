package com.olinnova.mentordoctor.ia;


import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ResponseStream;

import java.io.IOException;

public class Maestro {

    String projectId = "mentor-379";
    String location = "us-central1";
    String modelName = "gemini-1.5-flash-preview-0514";
    private static final String PROJECT_ID = "mentor-379";
    private static final String LOCATION = "us-central1";

    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION);) {

            GenerativeModel model = new GenerativeModel("gemini-1.5-flash-preview-0514", vertexAi);

            GenerateContentResponse response = model.generateContent("How are you?");
            System.out.println("Example de impresion response" + response);
            // Do something with the response

            ResponseStream<GenerateContentResponse> responseStream = model.generateContentStream("How are you?");
        }
    }
}

