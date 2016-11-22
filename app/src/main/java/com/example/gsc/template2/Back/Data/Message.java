package com.example.gsc.template2.Back.Data;

import com.backendless.BackendlessUser;

import java.util.Date;

/**
 * Created by GSC on 22/11/2016.
 */

public class Message {
    private String objectId;

    BackendlessUser sender ;
    BackendlessUser receiver ;
    String senderemail;
    String receiveremail;

    String message ;

    private Date created;



    public Message() {


    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public BackendlessUser getSender() {
        return sender;
    }

    public void setSender(BackendlessUser sender) {
        this.sender = sender;
    }

    public BackendlessUser getReceiver() {
        return receiver;
    }

    public void setReceiver(BackendlessUser receiver) {
        this.receiver = receiver;
    }

    public String getSenderemail() {
        return senderemail;
    }

    public void setSenderemail(String senderemail) {
        this.senderemail = senderemail;
    }

    public String getReceiveremail() {
        return receiveremail;
    }

    public void setReceiveremail(String receiveremail) {
        this.receiveremail = receiveremail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
