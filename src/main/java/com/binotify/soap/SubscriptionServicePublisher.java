package com.binotify.soap;

import jakarta.xml.ws.Endpoint;

import java.io.FileInputStream;
import java.io.IOException;

public class SubscriptionServicePublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8000/subscription", new SubscriptionServiceImpl());

        try {
            String configFilePath = "src/main/resources/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            SubscriptionServiceImpl.prop.load(propsInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
