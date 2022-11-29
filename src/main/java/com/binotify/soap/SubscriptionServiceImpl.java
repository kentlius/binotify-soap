package com.binotify.soap;

import jakarta.jws.WebService;

import java.sql.*;
import java.util.ArrayList;

@WebService(endpointInterface = "com.binotify.soap.SubscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public String createSub(int creator_id, int subscriber_id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/binotifysoap?autoReconnect=true&useSSL=false", "root", "password");
            PreparedStatement ps;
            ps = conn.prepareStatement("INSERT INTO subscription (creator_id, subscriber_id) VALUES (?, ?)");
            ps.setInt(1, creator_id);
            ps.setInt(2, subscriber_id);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "Subscription created";
    }

    @Override
    public String updateStatus(int creator_id, int subscriber_id, Status status) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/binotifysoap?autoReconnect=true&useSSL=false", "root", "password");
            PreparedStatement ps;
            ps = conn.prepareStatement("UPDATE subscription SET status = ? WHERE creator_id = ? AND subscriber_id = ?");
            ps.setString(1, status.toString());
            ps.setInt(2, creator_id);
            ps.setInt(3, subscriber_id);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "Status updated";
    }

    @Override
    public Status getStatus(int creator_id, int subscriber_id) {
        Status status = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/binotifysoap?autoReconnect=true&useSSL=false", "root", "password");
            PreparedStatement ps;
            ps = conn.prepareStatement("SELECT status FROM subscription WHERE creator_id = ? AND subscriber_id = ?");
            ps.setInt(1, creator_id);
            ps.setInt(2, subscriber_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                status = Status.valueOf(rs.getString("status"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public ArrayList<Subscription> getSubs() {
        ArrayList<Subscription> subs = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/binotifysoap?autoReconnect=true&useSSL=false", "root", "password");
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM subscription");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Subscription sub = new Subscription();
                sub.setCreator_id(rs.getInt("creator_id"));
                sub.setSubscriber_id(rs.getInt("subscriber_id"));
                sub.setStatus(Status.valueOf(rs.getString("status")));
                subs.add(sub);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return subs;
    }
}
