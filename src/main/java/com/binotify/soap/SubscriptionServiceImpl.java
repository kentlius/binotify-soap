package com.binotify.soap;

import com.sun.net.httpserver.HttpExchange;
import jakarta.annotation.Resource;
import jakarta.jws.WebService;
import jakarta.xml.ws.WebServiceContext;

import java.net.InetSocketAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@WebService(endpointInterface = "com.binotify.soap.SubscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {
    protected static Properties prop = new Properties();
    @Resource
    WebServiceContext context;

    @Override
    public Subscription createSub(String apiKey, int creator_id, int subscriber_id) {
        if (!apiKey.equals(prop.getProperty("api.key"))) {
            return null;
        }
        Subscription sub = new Subscription();
        sub.setCreator_id(creator_id);
        sub.setSubscriber_id(subscriber_id);
        sub.setStatus(Status.PENDING);
        HttpExchange exchange = (HttpExchange) context.getMessageContext().get("com.sun.xml.ws.http.exchange");
        InetSocketAddress remoteAddress = exchange.getRemoteAddress();
        String remoteHost = String.valueOf(remoteAddress.getAddress());
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + prop.getProperty("db.name") + "?allowPublicKeyRetrieval=true&useSSL=false", prop.getProperty("db.user"), prop.getProperty("db.password"));
            PreparedStatement ps = conn.prepareStatement("INSERT INTO subscription (creator_id, subscriber_id) VALUES (?, ?)");
            ps.setInt(1, creator_id);
            ps.setInt(2, subscriber_id);
            ps.executeUpdate();

            // Logger
            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO logging (description, IP, endpoint) VALUES (?, ?, ?)");
            ps2.setString(1, "Create subscription");
            ps2.setString(2, remoteHost);
            ps2.setString(3, "/subscription");
            ps2.executeUpdate();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sub;
    }

    @Override
    public Subscription updateStatus(String apiKey, int creator_id, int subscriber_id, Status status) {
        if (!apiKey.equals(prop.getProperty("api.key"))) {
            return null;
        }
        Subscription sub = new Subscription();
        sub.setCreator_id(creator_id);
        sub.setSubscriber_id(subscriber_id);
        sub.setStatus(status);
        HttpExchange exchange = (HttpExchange) context.getMessageContext().get("com.sun.xml.ws.http.exchange");
        InetSocketAddress remoteAddress = exchange.getRemoteAddress();
        String remoteHost = String.valueOf(remoteAddress.getAddress());
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + prop.getProperty("db.name") + "?allowPublicKeyRetrieval=true&useSSL=false", prop.getProperty("db.user"), prop.getProperty("db.password"));
            PreparedStatement ps = conn.prepareStatement("UPDATE subscription SET status = ? WHERE creator_id = ? AND subscriber_id = ?");
            ps.setString(1, status.toString());
            ps.setInt(2, creator_id);
            ps.setInt(3, subscriber_id);
            ps.executeUpdate();
            // Logger
            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO logging (description, IP, endpoint) VALUES (?, ?, ?)");
            ps2.setString(1, "Update subscription status");
            ps2.setString(2, remoteHost);
            ps2.setString(3, "/subscription");
            ps2.executeUpdate();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sub;
    }

    @Override
    public Status getStatus(String apiKey, int creator_id, int subscriber_id) {
        if (!apiKey.equals(prop.getProperty("api.key"))) {
            return null;
        }
        HttpExchange exchange = (HttpExchange) context.getMessageContext().get("com.sun.xml.ws.http.exchange");
        InetSocketAddress remoteAddress = exchange.getRemoteAddress();
        String remoteHost = String.valueOf(remoteAddress.getAddress());
        Status status = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + prop.getProperty("db.name") + "?allowPublicKeyRetrieval=true&useSSL=false", prop.getProperty("db.user"), prop.getProperty("db.password"));
            PreparedStatement ps = conn.prepareStatement("SELECT status FROM subscription WHERE creator_id = ? AND subscriber_id = ?");
            ps.setInt(1, creator_id);
            ps.setInt(2, subscriber_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                status = Status.valueOf(rs.getString("status"));
            }
            // Logger
            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO logging (description, IP, endpoint) VALUES (?, ?, ?)");
            ps2.setString(1, "Get status");
            ps2.setString(2, remoteHost);
            ps2.setString(3, "/subscription");
            ps2.executeUpdate();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public List<Subscription> getSubs(String apiKey) {
        if (!apiKey.equals(prop.getProperty("api.key"))) {
            return null;
        }
        List<Subscription> subs = new ArrayList<>();
        HttpExchange exchange = (HttpExchange) context.getMessageContext().get("com.sun.xml.ws.http.exchange");
        InetSocketAddress remoteAddress = exchange.getRemoteAddress();
        String remoteHost = String.valueOf(remoteAddress.getAddress());
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + prop.getProperty("db.name") + "?allowPublicKeyRetrieval=true&useSSL=false", prop.getProperty("db.user"), prop.getProperty("db.password"));
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM subscription");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Subscription sub = new Subscription();
                sub.setCreator_id(rs.getInt("creator_id"));
                sub.setSubscriber_id(rs.getInt("subscriber_id"));
                sub.setStatus(Status.valueOf(rs.getString("status")));
                subs.add(sub);
            }
            // Logger
            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO logging (description, IP, endpoint) VALUES (?, ?, ?)");
            ps2.setString(1, "Get subscriptions");
            ps2.setString(2, remoteHost);
            ps2.setString(3, "/subscription");
            ps2.executeUpdate();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return subs;
    }
}
