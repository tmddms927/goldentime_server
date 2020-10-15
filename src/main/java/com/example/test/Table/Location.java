package com.example.test.Table;

import lombok.*;
import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
@Entity
@Builder @AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue // Auto IncId Generator
    private int USER_NUM;

    @Column(length = 15)
    private String LONGITUDE;

    @Column(length = 15)
    private String LATITUDE;

    @Column(length = 200)
    private String FIREBASE_TOKEN;

    @Column
    private Date UPDATE_LOCA_T;
}