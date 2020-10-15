package com.example.test.Respositories;

import com.example.test.Table.Emergency_report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

public interface Call_reportResposity extends JpaRepository<Emergency_report, Long> {
    @Modifying
    @Transactional
    @Query(value="INSERT INTO CALL_REPORT(C_CALLER_NUM, C_RECIPIENT_NUM, C_REPORT_TIME, C_CALL_TIME, C_E_REPORT_NUM \n)\n"
            + "VALUES(?1, ?2, ?3, ?4, ?5)", nativeQuery=true)
    void user_create(int c_caller_num, int c_recipient_num, String c_report_time, String c_call_time, int c_e_report_num);

    @Transactional
    @Query(value="SELECT COUNT(*) FROM CALL_REPORT WHERE C_CALLER_NUM = ?1 AND C_RECIPIENT_NUM = ?2 AND C_REPORT_TIME = ?3 AND C_CALL_TIME = ?4 AND C_E_REPORT_NUM = ?5", nativeQuery=true)
    int checkC_REPORT_NUM(int c_caller_num, int c_recipient_num, String c_report_time, String c_call_time, int c_e_report_num);
}