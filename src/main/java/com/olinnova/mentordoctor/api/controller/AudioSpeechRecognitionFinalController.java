package com.olinnova.mentordoctor.api.controller;

import com.olinnova.mentordoctor.api.service.mentor.AudioSpeechRecognitionFinaService;
import com.olinnova.mentordoctor.api.service.mentor.AudioSpeechRecognitionService;
import com.olinnova.mentordoctor.dto.RequestAudioDto;
import com.olinnova.mentordoctor.dto.ResponseAudioDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/audiocapture/final")
public class AudioSpeechRecognitionFinalController {

    private final Environment environment;
    private final AudioSpeechRecognitionFinaService audioSpeechRecognitionFinaService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AudioSpeechRecognitionFinalController.class);

    @Autowired
    public AudioSpeechRecognitionFinalController(Environment environment,
                                                 AudioSpeechRecognitionFinaService audioSpeechRecognitionFinaService) {
        this.environment = environment;
        this.audioSpeechRecognitionFinaService = audioSpeechRecognitionFinaService;
    }

    @PostMapping(value = "/audio/speech/recognition", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<ResponseAudioDto> requestProcessAudio(@RequestBody RequestAudioDto requestAudioDto) {
       // LOGGER.debug("Processing AccountAPIController: {}", requestAudioDto);
        //LOGGER.info("Lo que trae el request de Json: {}", requestAudioDto);

        return audioSpeechRecognitionFinaService.audioSpeechRecognitionFinalProcess(requestAudioDto);
    }




}
