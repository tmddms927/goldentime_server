package com.example.test.Table;

import lombok.*;
import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity @Builder
@AllArgsConstructor
public class Identity {
    @Id
    @GeneratedValue // Auto IncId Generator
    private int USER_NUM;

    @NonNull
    @Column(length = 20)
    private String USER_ID;

    @NonNull
    @Column(length = 20)
    private String NAME;

    @NonNull
    @Column
    private Date BIRTHDAY;

    @NonNull
    @Column(length = 20)
    private String PHONE;

    @Column(length = 30)
    private String EMAIL;

    @NonNull
    @Column(length = 20)
    private String PASSWORD;

    @Column(columnDefinition = "boolean default false")
    private Boolean DOCTOR;

    @Column(columnDefinition = "boolean default false")
    private Boolean NURSE;
}