package com.olinnova.mentordoctor.util;

public class CommonPrompt {


    public  static final String GCP_PROJECT_ID = "spring.application.project-id";
    public  static final String GCP_LOCATION = "spring.application.location";
    public  static final String GCP_GENERATIVE_MODEL = "spring.application.generative-model-name";

    private CommonPrompt(){

        throw new UnsupportedOperationException("This is a utility class and cannot be intantiated");
    }
}
