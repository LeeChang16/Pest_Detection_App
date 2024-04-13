package com.example.pestdetectionapp;

import android.util.Base64;

import java.io.IOException;

import retrofit2.Converter;

public class ByteArrayToStringConverter extends Converter.Factory implements Converter<byte[], String> {
    @Override
    public String convert(byte[] value) throws IOException {
        return Base64.encodeToString(value, Base64.DEFAULT);
    }
}
