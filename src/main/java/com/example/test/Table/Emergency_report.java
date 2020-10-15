package com.example.test.Table;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity @Builder
@AllArgsConstructor
public class Emergency_report {
    @Id
    @GeneratedValue // Auto IncId Generator
    private int E_REPORT_NUM;

    @NonNull
    @Column
    private int E_PATIENT_NUM;

    @NonNull
    @Column
    private Date E_REPORT_TIME;

    @NonNull
    @Column(length = 20)
    private String E_REPORT_LONGITUDE;

    @NonNull
    @Column(length = 20)
    private String E_REPORT_LATITUDE;

    @Column
    private int E_SAVIOR_NUM;

    @Column(columnDefinition = "boolean default false")
    private Boolean E_DID_CPR;

    @Column(columnDefinition = "boolean default false")
    private Boolean E_USED_AED;

}
