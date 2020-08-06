package com.thoughtworks.rslist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RsEvent {
    @NotNull
    private String eventName;
    @NotNull
    private String keyword;
    @NotNull
    private Integer userId;
    private int voteNum;

    //    @JsonIgnore
    public Integer getUserId() {
        return userId;
    }

    //    @JsonProperty
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
