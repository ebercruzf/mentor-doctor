package com.olinnova.mentordoctor.api.service.mentor;

import com.olinnova.mentordoctor.dto.RequestAudioDto;
import com.olinnova.mentordoctor.dto.ResponseAudioDto;

import com.olinnova.mentordoctor.dto.ResponseGenericDTO;
import reactor.core.publisher.Mono;

public interface AudioSpeechRecognitionService {

    public Mono<ResponseGenericDTO> audioSpeechRecognitionProcess(RequestAudioDto requestBody);

}
