package com.binotify.soap;

import jakarta.xml.ws.Endpoint;

public class SubscriptionServicePublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/subscription", new SubscriptionServiceImpl());
    }
}
