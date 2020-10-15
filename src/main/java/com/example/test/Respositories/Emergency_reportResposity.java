package com.example.test.Respositories;

import com.example.test.Table.Emergency_report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

public interface Emergency_reportResposity extends JpaRepository<Emergency_report, Long> {

    @Modifying
    @Transactional
    @Query(value="INSERT INTO EMERGENCY_REPORT(E_PATIENT_NUM, E_REPORT_TIME, E_REPORT_LONGITUDE, E_REPORT_LATITUDE, E_SAVIOR_NUM)\n"
            + "VALUES(?1, ?2, ?3, ?4, ?1)", nativeQuery=true)
    void user_create(int e_patient_num, String e_report_time, String e_report_longitude, String e_report_latitude);

    @Transactional
    @Query(value="SELECT * FROM EMERGENCY_REPORT WHERE E_PATIENT_NUM = ?1 AND E_REPORT_TIME = ?2 AND E_REPORT_LONGITUDE = ?3 AND E_REPORT_LATITUDE = ?4", nativeQuery=true)
    Emergency_report checkE_REPORT_NUM(int e_patient_num, String e_report_time, String e_report_longitude, String e_report_latitude);

    @Transactional
    @Query(value="SELECT * FROM EMERGENCY_REPORT WHERE E_REPORT_NUM = ?1", nativeQuery=true)
    Emergency_report checkUsernum(int e_report_num);

    @Modifying
    @Transactional
    @Query(value="UPDATE EMERGENCY_REPORT SET E_SAVIOR_NUM = ?2 WHERE E_REPORT_NUM = ?1", nativeQuery=true)
    void updateSaviorNum(int e_report_num, int e_savior_num);
}