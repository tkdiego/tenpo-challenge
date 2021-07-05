package com.taka.tenpo.domain.recorder.model;


import com.taka.tenpo.domain.recorder.enums.RequestStatus;
import com.taka.tenpo.domain.recorder.enums.RequestType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tracking_recorder")
@Getter
@Setter
@ToString
public class TrackingRecorder {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String username;

    @Enumerated(STRING)
    @Column(name = "request_type")
    private RequestType requestType;

    @CreationTimestamp
    private OffsetDateTime date;

    @Enumerated(STRING)
    @Column(name = "request_status")
    private RequestStatus requestStatus;

}
