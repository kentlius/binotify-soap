package com.binotify.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

import java.util.List;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface SubscriptionService {
    @WebMethod
    @WebResult(name = "subscription")
    Subscription createSub(@WebParam(name = "apiKey", header = true) String apiKey, @WebParam(name = "creator_id") int creator_id, @WebParam(name = "subscriber_id") int subscriber_id);

    @WebMethod
    @WebResult(name = "subscription")
    Subscription updateStatus(@WebParam(name = "apiKey", header = true) String apiKey, @WebParam(name = "creator_id") int creator_id, @WebParam(name = "subscriber_id") int subscriber_id, @WebParam(name = "status") Status status);

    @WebMethod
    @WebResult(name = "status")
    Status getStatus(@WebParam(name = "apiKey", header = true) String apiKey, @WebParam(name = "creator_id") int creator_id, @WebParam(name = "subscriber_id") int subscriber_id);

    @WebMethod
    @WebResult(name = "subscription")
    List<Subscription> getSubs(@WebParam(name = "apiKey", header = true) String apiKey);
}
