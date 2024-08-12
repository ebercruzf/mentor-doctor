package com.olinnova.mentordoctor.api.controller;

import com.olinnova.mentordoctor.api.service.mentor.AudioSpeechRecognitionService;
import com.olinnova.mentordoctor.api.service.mentor.TextSpeechFindService;
import com.olinnova.mentordoctor.dto.RequestAudioDto;
import com.olinnova.mentordoctor.dto.RequestTxtDto;
import com.olinnova.mentordoctor.dto.ResponseGenericDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/audiocapture")
public class TextSpeechFindController {

    private final Environment environment;

    private final TextSpeechFindService textSpeechFindService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TextSpeechFindController.class);

    @Autowired
    public TextSpeechFindController(Environment environment, TextSpeechFindService textSpeechFindService) {
        this.environment = environment;

        this.textSpeechFindService = textSpeechFindService;
    }

    @PostMapping(value = "/txt/speech/recognition", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public Mono<ResponseGenericDTO> requestProcessAudio(@RequestBody RequestTxtDto requestAudioDto) {
        LOGGER.debug("Processing AccountAPIController: {}", requestAudioDto);
        LOGGER.info("Lo que trae el request de Json: {}", requestAudioDto);

        return textSpeechFindService.textSpeechFindProcess(requestAudioDto);
    }




}
