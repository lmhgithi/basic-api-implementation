package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "vote")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteEntity {
    @Id
    @GeneratedValue
    private Integer voteId;
    private LocalTime localTime;
    private int voteNum;

    @ManyToOne
    @JoinColumn(name = "rsId")
    private RsEntity rs;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;
}
