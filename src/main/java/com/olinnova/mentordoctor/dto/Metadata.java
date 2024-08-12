package com.olinnova.mentordoctor.dto;

public class Metadata {


    public Metadata() {

    }
    public Metadata(String fecha, String hora) {
        this.fecha = fecha;
        this.hora = hora;
    }

    private String fecha;
    private String hora;


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                '}';
    }
}
