package com.example.test.Respositories;

import com.example.test.Table.Health;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

public interface HealthRespository extends JpaRepository<Health, Long> {

    @Modifying
    @Transactional
    @Query(value="INSERT INTO HEALTH VALUES(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)", nativeQuery=true)
    void user_create(int user_num, String gender, String blood_pressure, String allergy, String chronic_disease, String protector_phone, String protector_relation, String blood_type);

    @Transactional
    @Query(value="SELECT * FROM HEALTH WHERE USER_NUM = ?1", nativeQuery=true)
    Health searchProtectorPhone(int user_num);

    @Transactional
    @Query(value="SELECT * FROM HEALTH WHERE USER_NUM = ?1", nativeQuery=true)
    Health userinfo(int user_num);

    @Modifying
    @Transactional
    @Query(value="UPDATE HEALTH SET GENDER = ?2, BLOOD_PRESSURE = ?3, ALLERGY = ?4, CHRONIC_DISEASE = ?5, PROTECTOR_PHONE = ?6, PROTECTOR_RELATION = ?7, BLOOD_TYPE = ?8\n"
            + "WHERE USER_NUM = ?1", nativeQuery=true)
    void update_UserInfo(int user_num, String gender, String blood_pressure, String allergy, String chronic_disease, String protector_phone, String protector_relation, String blood_type);

    @Transactional
    @Query(value="SELECT COUNT(*) FROM HEALTH\n"
            + "WHERE USER_NUM = ?1 AND GENDER = ?2 AND BLOOD_PRESSURE = ?3 AND ALLERGY = ?4 AND CHRONIC_DISEASE = ?5 AND PROTECTOR_PHONE = ?6 AND PROTECTOR_RELATION = ?7 AND BLOOD_TYPE = ?8", nativeQuery=true)
    int update_check(int user_num, String gender, String blood_pressure, String allergy, String chronic_disease, String protector_phone, String protector_relation, String blood_type);
}