package com.olinnova.mentordoctor.api.service.mentor;

import com.olinnova.mentordoctor.dto.RequestAudioDto;
import com.olinnova.mentordoctor.dto.ResponseAudioDto;
import reactor.core.publisher.Mono;

public interface AudioSpeechRecognitionFinaService {

    public Mono<ResponseAudioDto> audioSpeechRecognitionFinalProcess(RequestAudioDto requestBody);

}
