//package com.olinnova.mentordoctor.api.service.gcp;
//
//import com.google.cloud.speech.v1.*;
//import com.google.protobuf.ByteString;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//
//public class SendToGeminiWavToTxtServiceTemp {
//
//    public String sendToGemini ( String audioWav){
//
////        String audioFilePath = "src/main/resources/audio/output.wav";
//
//        System.out.println("Trying to access file: " + audioWav);
//        // Validar si el archivo existe
//        Path audioPath = Path.of(audioWav);
//        System.out.println("Trying to access file: " + audioPath.toAbsolutePath());
//        if (Files.exists(audioPath)) {
//            System.out.println("File exists!");
//
//            // Comprobar si se puede acceder al archivo (lectura)
//            if (Files.isReadable(audioPath)) {
//                System.out.println("File is accessible for reading.");
//                // resto de tu código para transcribir el audio
//            } else {
//                System.out.println("File exists but cannot be accessed for reading.");
//            }
//        } else {
//            System.out.println("File does not exist.");
//        }
//
//        SpeechRecognitionAlternative alternative = null;
//
//        try (var speechClient = SpeechClient.create()) {
//            // Configurar la solicitud de reconocimiento de voz
//            var config = RecognitionConfig.newBuilder()
//                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
//                    .setSampleRateHertz(44100)
//                    .setLanguageCode("es-ES") // Configurar el idioma deseado
//                    .build();
//
//            // Leer el archivo de audio
//            byte[] audioBytes = Files.readAllBytes(Path.of(audioWav));
//            var audio = RecognitionAudio.newBuilder()
//                    .setContent(ByteString.copyFrom(audioBytes))
//                    .build();
//
//            // Enviar la solicitud de reconocimiento de voz
//            RecognizeResponse response = speechClient.recognize(config, audio);
//
//            // Procesar los resultados
//            for (var result : response.getResultsList()) {
//                alternative = result.getAlternativesList().get(0);
//                System.out.println("Transcripción Gemini: " + alternative.getTranscript());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return alternative.getTranscript().toString();
//
//    }
//
//}
