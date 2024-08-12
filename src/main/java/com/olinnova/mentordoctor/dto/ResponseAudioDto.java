package com.olinnova.mentordoctor.dto;

public class ResponseAudioDto {

    private String format;
    private String sampleRate;
    private String respuestaAudioBase64;
    private String respuestaTexto;
    private String code;


    public ResponseAudioDto() {

    }

    public ResponseAudioDto(String format, String sampleRate, String respuestaAudioBase64, String respuestaTexto, String code) {
        this.format = format;
        this.sampleRate = sampleRate;
        this.respuestaAudioBase64 = respuestaAudioBase64;
        this.respuestaTexto = respuestaTexto;
        this.code = code;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(String sampleRate) {
        this.sampleRate = sampleRate;
    }

    public String getRespuestaAudioBase64() {
        return respuestaAudioBase64;
    }

    public void setRespuestaAudioBase64(String respuestaAudioBase64) {
        this.respuestaAudioBase64 = respuestaAudioBase64;
    }

    public String getRespuestaTexto() {
        return respuestaTexto;
    }

    public void setRespuestaTexto(String respuestaTexto) {
        this.respuestaTexto = respuestaTexto;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResponseAudioDto{" +
                "format='" + format + '\'' +
                ", sampleRate='" + sampleRate + '\'' +
                ", respuestaAudioBase64='" + respuestaAudioBase64 + '\'' +
                ", respuestaTexto='" + respuestaTexto + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

}
