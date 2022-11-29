package com.binotify.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

import java.util.ArrayList;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface SubscriptionService {
    @WebMethod
    String createSub(int creator_id, int subscriber_id);

    @WebMethod
    String updateStatus(int creator_id, int subscriber_id, Status status);

    @WebMethod
    Status getStatus(int creator_id, int subscriber_id);

    @WebMethod
    ArrayList<Subscription> getSubs();
}
