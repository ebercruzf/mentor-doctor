package com.olinnova.mentordoctor.ia;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.*;

import java.io.IOException;

public class MaestroMultimodal {

    String projectId = "mentor-379";
    String location = "us-central1";
    static String modelName = "gemini-1.5-flash-preview-0514";
    private static final String PROJECT_ID = "mentor-379";
    private static final String LOCATION = "us-central1";

    public static void main(String[] args) throws IOException {
        try (VertexAI vertexAi = new VertexAI(PROJECT_ID, LOCATION);) {

            //GenerativeModel model = new GenerativeModel("gemini-1.5-flash-preview-0514", vertexAi);

            //GenerateContentResponse response = model.generateContent("How are you?");
            //System.out.println("Example de impresion response" + response);
            // Do something with the response

           // ResponseStream<GenerateContentResponse> responseStream = model.generateContentStream("How are you?");


            String videoUri = "gs://cloud-samples-data/video/animals.mp4";

            GenerativeModel model = new GenerativeModel(modelName, vertexAi);
            GenerateContentResponse response = model.generateContent(
                    ContentMaker.fromMultiModalData(
                            "What is in the video?",
                            PartMaker.fromMimeTypeAndData("video/mp4", videoUri)
                    ));

            String output = ResponseHandler.getText(response);
            System.out.println("Respuesta: " + output);


        }
    }
}
