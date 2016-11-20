package com.example.gsc.template2.Back.Data;

import com.backendless.BackendlessUser;

import java.util.Date;

/**
 * Created by GSC on 19/11/2016.
 */

public class Request {

    private String objectId;

    BackendlessUser sender ;
    BackendlessUser receiver ;
    String senderemail;
    String receiveremail;

    private Date created;
    private Date updated;

    public Request(String objectId, BackendlessUser sender, BackendlessUser receiver, Date created, Date updated) {
        this.objectId = objectId;
        this.sender = sender;
        this.receiver = receiver;

        this.created = created;
        this.updated = updated;

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

    public Request () {}

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}

