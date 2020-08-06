package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;

@Entity
@Table(name = "rs_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsEntity {
    @Id
    @GeneratedValue
    private Integer rsId;
    private String eventName;
    private String keyword;
    private String userId;
    private int voteNum;
}
