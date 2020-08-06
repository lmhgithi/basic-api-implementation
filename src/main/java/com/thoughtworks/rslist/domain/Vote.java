package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.security.Timestamp;
import java.time.LocalTime;
import java.util.Locale;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Vote {
    private int userId;
    private int rsId;
    private Timestamp timestamp;
    private int voteNum;
}
