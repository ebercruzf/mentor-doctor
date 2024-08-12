package com.olinnova.mentordoctor.api.service.mentor.impl;


import com.google.cloud.speech.v1.*;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.Part;
import com.google.cloud.vertexai.generativeai.ResponseStream;
import com.google.protobuf.ByteString;
import com.olinnova.mentordoctor.api.service.gcp.SendToGeminiWavToTxtService;
import com.olinnova.mentordoctor.api.service.mentor.AudioSpeechRecognitionFinaService;
import com.olinnova.mentordoctor.api.service.gcp.SendGeminiTextDataService;
//import com.olinnova.mentordoctor.api.service.gcp.SendToGeminiWavToTxtServiceTemp;
import com.olinnova.mentordoctor.api.service.gcp.impl.SendGeminiTextDataServiceImpl;
import com.olinnova.mentordoctor.dto.RequestAudioDto;
import com.olinnova.mentordoctor.dto.ResponseAudioDto;
import com.olinnova.mentordoctor.util.TemplateData;
import com.olinnova.mentordoctor.util.TemplateProcessor;
import com.olinnova.mentordoctor.util.WebmToWavConverter;
import org.bytedeco.ffmpeg.avutil.LogCallback;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;


@Service
public class AudioSpeechRecognitionFinalServiceImpl implements AudioSpeechRecognitionFinaService {

    private final SendGeminiTextDataService sendGeminiTextDataService;

    private final SendToGeminiWavToTxtService sendToGeminiWavToTxtService;

    public AudioSpeechRecognitionFinalServiceImpl(SendGeminiTextDataService sendGeminiTextDataService, SendToGeminiWavToTxtService sendToGeminiWavToTxtService) {
        this.sendGeminiTextDataService = sendGeminiTextDataService;
        this.sendToGeminiWavToTxtService = sendToGeminiWavToTxtService;
    }

    @Override
    public Mono<ResponseAudioDto> audioSpeechRecognitionFinalProcess(RequestAudioDto requestBody) {

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

        //SendToGeminiWavToTxtServiceTemp sendToGeminiWavToTxtServiceTemp = new SendToGeminiWavToTxtServiceTemp();
        //String respuesta = sendToGemini(outputFileWav);
        String respuesta = sendToGeminiWavToTxtService.sendToGemini(outputFileWav);

        SendGeminiTextDataServiceImpl sendGeminiTextDataServiceImpl;

       // sendGeminiTextDataServiceImpl = new SendGeminiTextDataServiceImpl();
//###################################
        ResponseStream<GenerateContentResponse> responseStream = null;
        try {

          responseStream = sendGeminiTextDataService.questionTextAsynchronous(respuesta);



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Respuesta_de_pregunta_de_voz_a_texto_yaConRespuesta.: " + responseStream);
        ResponseAudioDto responseAudioDto = new ResponseAudioDto();
        responseAudioDto.setRespuestaTexto(respuesta);

        return Mono.just(responseAudioDto);
    }


    public String sendToGemini ( String audioWav){

//        String audioFilePath = "src/main/resources/audio/output.wav";

        System.out.println("Trying to access file: " + audioWav);
        // Validar si el archivo existe

        Path audioPath = Path.of(audioWav);
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

        SpeechRecognitionAlternative alternative = null;

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
        return alternative.getTranscript().toString();

    }


//    public String webMToWAVConverter(String base64AudioWebmOrigin) throws IOException {
//
//        System.out.println("Ejemplodelbase64: " + base64AudioWebmOrigin);
//        String outputFile = "output.wav";
//        String audioFilePath = "src/main/resources/audio/output.wav"; // Ruta del archivo WAV de salida
//
//        // Create the output file if it doesn't exist
//        File audioFile = new File(audioFilePath);
//        if (!audioFile.exists()) {
//            if (!audioFile.createNewFile()) {
//                throw new IOException("Error creating file: " + audioFile.getAbsolutePath());
//            }
//        }
//
//        try {
//
//            FFmpegLogCallback callback = new MyFFmpegLogCallback();
//            FFmpegLogCallback.set();
//
//            // Integrate FFmpegLogCallback (Optional for more detailed logs)
//
//
//            //byte[] webmBytes = Base64.getDecoder().decode(base64AudioWebmOrigin);
//            byte[] webmBytes = Base64.getDecoder().decode(base64AudioWebmOrigin);
//            if (webmBytes.length == 0) {
//                throw new IOException("Error: Decoded WebM data is empty");
//            }
//            // Crear un objeto FFmpegFrameGrabber para leer el audio WebM desde los bytes
//            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new ByteArrayInputStream(webmBytes));
//            //
//             grabber.start(); // Invocar el método start() en la instancia de FFmpegFrameGrabber
//
//
//
//            // Crear un objeto FFmpegFrameRecorder para escribir el archivo WAV
//            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(audioFilePath, grabber.getAudioChannels());
//            recorder.start(); // Invocar el método start() en la instancia de FFmpegFrameRecorder
//
//
//
//            // Read audio frames and write to WAV file
//            org.bytedeco.javacv.Frame frame;
//            while ((frame = grabber.grabFrame()) != null) {
//                if (frame.data != null) { // Compruebe si hay nulos antes de acceder a los datos
//                    recorder.recordSamples(frame.data);
//                }
//            }
//
//            // Stop and release resources (moved within the try block)
//            recorder.stop();
//            grabber.stop();
//
//        } catch (Exception e) {
//            // Handle exceptions more specifically (e.g., log error details)
//            e.printStackTrace();
//            throw new IOException("Error converting WebM to WAV: " + e.getMessage());
//        }
//
//
//        return outputFile;
//    }

    public static void convertWebmToWav(String base64WebmData, String outputFilePath) throws IOException {
        // Configurar el registro detallado de FFmpeg
        //av_log_set_level(AV_LOG_DEBUG);
        //av_log_set_callback(new MiLogCallback());
        FFmpegLogCallback.set();

        byte[] webmBytes = Base64.getDecoder().decode(base64WebmData);



        try (


                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new ByteArrayInputStream(webmBytes));
                FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFilePath, grabber.getAudioChannels())


        ) {

//            grabber.start();
//            recorder.start();
                grabber.start();
                recorder.start();
                recorder.stop();
                grabber.stop();

            org.bytedeco.javacv.Frame frame;
            while ((frame = grabber.grabFrame()) != null) {
                if (frame.data != null) {
                    recorder.recordSamples(frame.data);
                }
            }
        }
    }

    private static class MiLogCallback extends LogCallback {

        public void call(BytePointer pointer, int level, PointerPointer pointerPointer) {
            if (pointerPointer != null) {
                BytePointer messagePointer = (BytePointer) pointerPointer.get(BytePointer.class, 0);
                if (messagePointer != null) {
                    String logMessage = messagePointer.getString();
                    System.out.println(logMessage);
                }
            }
        }
    }













}
