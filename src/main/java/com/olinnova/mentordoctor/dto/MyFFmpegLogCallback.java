package com.olinnova.mentordoctor.dto;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacv.FFmpegLogCallback;

public class MyFFmpegLogCallback extends FFmpegLogCallback {
    public MyFFmpegLogCallback() {
        super();
    }

    public void log(BytePointer pointer) {
        System.out.println(pointer.getString());
    }
}
