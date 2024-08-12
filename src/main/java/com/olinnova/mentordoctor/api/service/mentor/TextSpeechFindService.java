package com.olinnova.mentordoctor.api.service.mentor;

import com.olinnova.mentordoctor.dto.RequestAudioDto;
import com.olinnova.mentordoctor.dto.RequestTxtDto;
import com.olinnova.mentordoctor.dto.ResponseGenericDTO;
import reactor.core.publisher.Mono;

public interface TextSpeechFindService {


    public Mono<ResponseGenericDTO> textSpeechFindProcess(RequestTxtDto requestBody);

}
