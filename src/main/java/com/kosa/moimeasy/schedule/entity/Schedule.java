package com.kosa.moimeasy.schedule.entity;

import com.kosa.moimeasy.common.entity.BaseEntity;
import com.kosa.moimeasy.moeim.entity.Moeim;
import com.kosa.moimeasy.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID")
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "MOEIM_ID", nullable = false)
    private Moeim moeim;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User creator;

    @Column(name = "SCHEDULE_NAME", nullable = false)
    private String scheduleName;

    private String contents;

    @Column(name = "START_TIME", nullable = false)
    private Timestamp startTime;

    @Column(name = "END_TIME", nullable = false)
    private Timestamp endTime;

    @Column(name = "LOCATION")
    private String location;
}
