package com.example.test.Table;

import lombok.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity @Builder
@AllArgsConstructor
public class Call_report {
    @Id
    @GeneratedValue // Auto IncId Generator
    private int C_REPORT_NUM;

    @NonNull
    @Column
    private int C_RECIPIENT_NUM;

    @NonNull
    @Column
    private int C_CALLER_NUM;

    @NonNull
    @Column
    private Date C_REPORT_TIME;

    @NonNull
    @Column
    private Time C_CALL_TIME;
}