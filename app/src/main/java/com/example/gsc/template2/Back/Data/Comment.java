package com.example.gsc.template2.Back.Data;

import com.backendless.BackendlessUser;

import java.util.Date;

/**
 * Created by GSC on 07/12/2016.
 */

public class Comment {

    private String objectId;

    BackendlessUser sender ;

    String senderemail;
    String receiveremail;

    String message ;

    private Date created;

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
