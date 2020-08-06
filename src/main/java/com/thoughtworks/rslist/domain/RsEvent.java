package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public class RsEvent {
    @NotNull private String eventName;
    @NotNull private String keyword;
    @NotNull private String userId;

    public RsEvent() {
    }

    public RsEvent(String eventName, String keyword, String userId) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

//    @JsonIgnore
//    public User getUser() {
//        return user;
//    }
//
//    @JsonProperty
//    public void setUser(User user) {
//        this.user = user;
//    }
}
