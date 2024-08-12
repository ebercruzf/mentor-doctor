package com.olinnova.mentordoctor.util;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.info.AudioInfo;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;


public class WebmToWavConverter {
     public  void converter(String inputFileWebm, String outputFileWavMono) {
//        String inputFileWebm = "src/main/resources/audio/video.webm";
//        String outputFileWavMono = "src/main/resources/audio/output_mono.wav";

        try {
            // Crear objeto multimedia a partir del archivo WebM
            MultimediaObject multimediaObject = new MultimediaObject(new File(inputFileWebm));

            // Obtener información de audio del archivo WebM
            AudioInfo audioInfo = multimediaObject.getInfo().getAudio();

            // Configurar atributos de codificación para WAV mono
            AudioAttributes audioAttributes = new AudioAttributes();
            audioAttributes.setCodec("pcm_s16le");
            audioAttributes.setChannels(1); // Establecer a 1 canal (mono)
            audioAttributes.setSamplingRate(44100); //44100   audioInfo.getSamplingRate()

            EncodingAttributes encodingAttributes = new EncodingAttributes();
            encodingAttributes.setOutputFormat("wav");
            encodingAttributes.setAudioAttributes(audioAttributes);

            // Crear encoder y realizar la conversión
            Encoder encoder = new Encoder();
            encoder.encode(multimediaObject, new File(outputFileWavMono), encodingAttributes);

            System.out.println("Archivo WAV mono generado exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al convertir el archivo: " + e.getMessage());
        } // Llave de cierre agregada


         String wavFile = outputFileWavMono;
         try {
             AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(new File(wavFile));
             int numChannels = fileFormat.getFormat().getChannels();
             if (numChannels == 1) {
                 System.out.println("El archivo de audio es mono (1 canal)");
             } else if (numChannels == 2) {
                 System.out.println("El archivo de audio es estéreo (2 canales)");
             } else {
                 System.out.println("El archivo de audio tiene " + numChannels + " canales");
             }
         } catch (Exception e) {
             System.err.println("Error al leer el archivo de audio: " + e.getMessage());
         }
    }
}