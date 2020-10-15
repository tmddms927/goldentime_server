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
public class Health {
    @Id
    @GeneratedValue // Auto IncId Generator
    private int USER_NUM;

    @NonNull
    @Column(length = 4)
    private String GENDER;

    @NonNull
    @Column(length = 4)
    private String BLOOD_PRESSURE;

    @NonNull
    @Column(length = 100)
    private String ALLERGY;

    @NonNull
    @Column(length = 100)
    private String CHRONIC_DISEASE;

    @NonNull
    @Column(length = 20)
    private String PROTECTOR_PHONE;

    @NonNull
    @Column(length = 10)
    private String PROTECTOR_RELATION;

    @NonNull
    @Column(length = 10)
    private String BLOOD_TYPE;
}
