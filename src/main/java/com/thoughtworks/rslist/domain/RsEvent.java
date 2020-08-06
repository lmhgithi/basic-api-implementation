package com.thoughtworks.rslist.domain;

import lombok.Builder;

import javax.validation.constraints.NotNull;
@Builder
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
    public String getUserId() {
        return userId;
    }

//    @JsonProperty
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
