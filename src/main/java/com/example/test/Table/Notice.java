package com.example.test.Table;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity @Builder
@AllArgsConstructor
public class Notice {
    @Id
    @GeneratedValue // Auto IncId Generator
    private int USER_NUM;

    @Column
    private Boolean CPR;

    @Column
    private Boolean ALARM;
}
