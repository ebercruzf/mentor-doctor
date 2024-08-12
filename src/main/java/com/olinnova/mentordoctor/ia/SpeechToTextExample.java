package com.olinnova.mentordoctor.ia;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.nio.file.Files;
import java.nio.file.Path;

public class SpeechToTextExample {
    public static void main(String[] args) throws Exception {
        // Ruta del archivo de audio a transcribir
        String audioFilePath = "src/main/resources/audio/Nel-2.wav";

        System.out.println("Trying to access file: " + audioFilePath);
        // Validar si el archivo existe
        Path audioPath = Path.of(audioFilePath);
        System.out.println("Trying to access file: " + audioPath.toAbsolutePath());
        if (Files.exists(audioPath)) {
            System.out.println("File exists!");

            // Comprobar si se puede acceder al archivo (lectura)
            if (Files.isReadable(audioPath)) {
                System.out.println("File is accessible for reading.");
                // resto de tu código para transcribir el audio
            } else {
                System.out.println("File exists but cannot be accessed for reading.");
            }
        } else {
            System.out.println("File does not exist.");
        }

        try (var speechClient = SpeechClient.create()) {
            // Configurar la solicitud de reconocimiento de voz
            var config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(44100)
                    .setLanguageCode("es-ES") // Configurar el idioma deseado
                    .build();

            // Leer el archivo de audio
            byte[] audioBytes = Files.readAllBytes(Path.of(audioFilePath));
            var audio = RecognitionAudio.newBuilder()
                    .setContent(ByteString.copyFrom(audioBytes))
                    .build();

            // Enviar la solicitud de reconocimiento de voz
            RecognizeResponse response = speechClient.recognize(config, audio);

            // Procesar los resultados
            for (var result : response.getResultsList()) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.println("Transcripción: " + alternative.getTranscript());
            }
        }
    }
}