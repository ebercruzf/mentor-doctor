package com.olinnova.mentordoctor.api.service.gcp.impl;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import com.olinnova.mentordoctor.api.controller.AudioSpeechRecognitionFinalController;
import com.olinnova.mentordoctor.api.service.gcp.SendToGeminiWavToTxtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SendToGeminiWavToTxtServiceImp implements SendToGeminiWavToTxtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioSpeechRecognitionFinalController.class);

    @Override
    public String sendToGemini ( String audioWav){

        long startTime = System.currentTimeMillis();

//        String audioFilePath = "src/main/resources/audio/output.wav";

        SpeechRecognitionAlternative alternative = null;

        System.out.println("Trying to access file: " + audioWav);
        // Validar si el archivo existe
        Path audioPath = Path.of(audioWav);
        System.out.println("Trying to access file: " + audioPath.toAbsolutePath());
        if (Files.exists(audioPath)) {
            System.out.println("File exists!");

            // Comprobar si se puede acceder al archivo (lectura)
            if (Files.isReadable(audioPath)) {
                LOGGER.info("File is accessible for reading");
                try (var speechClient = SpeechClient.create()) {
                    // Configurar la solicitud de reconocimiento de voz
                    var config = RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setSampleRateHertz(44100)
                            .setLanguageCode("es-ES") // Configurar el idioma deseado
                            .build();

                    // Leer el archivo de audio
                    byte[] audioBytes = Files.readAllBytes(Path.of(audioWav));
                    var audio = RecognitionAudio.newBuilder()
                            .setContent(ByteString.copyFrom(audioBytes))
                            .build();

                    // Enviar la solicitud de reconocimiento de voz
                    RecognizeResponse response = speechClient.recognize(config, audio);

                    // Procesar los resultados
                    for (var result : response.getResultsList()) {
                        alternative = result.getAlternativesList().get(0);
                        System.out.println("Transcripción Gemini: " + alternative.getTranscript());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else {
                LOGGER.info("File exists but cannot be accessed for reading.");
                System.out.println("");
            }
        } else {
            System.out.println("File does not exist.");
        }


        long endTime = System.currentTimeMillis();  // Capture end time

        long responseTime = endTime - startTime;  // Calculate response time

        System.out.println("SendToGeminiWavToTxtServiceImp/sendToGemini Response time (ms): " + responseTime);


        return alternative.getTranscript().toString();

    }

}
