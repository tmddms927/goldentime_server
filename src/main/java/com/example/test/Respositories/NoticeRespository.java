package com.example.test.Respositories;

import com.example.test.Table.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

public interface NoticeRespository extends JpaRepository<Notice, Long> {

    @Modifying
    @Transactional
    @Query(value="INSERT INTO NOTICE VALUES(?1, ?2, ?3)", nativeQuery=true)
    void user_create(int user_num, Boolean cpr, Boolean alarm);

    @Transactional
    @Query(value="SELECT COUNT(*) FROM NOTICE WHERE USER_NUM = ?1", nativeQuery=true)
    int insert_check(int user_num);

    @Transactional
    @Query(value="SELECT * FROM NOTICE WHERE USER_NUM = ?1", nativeQuery=true)
    Notice userInfo(int user_num);

    @Modifying
    @Transactional
    @Query(value="UPDATE NOTICE SET CPR = ?1 WHERE USER_NUM = ?2", nativeQuery=true)
    void updateUserInfo(Boolean cpr, int user_num);

    @Transactional
    @Query(value="SELECT COUNT(*) FROM NOTICE WHERE CPR = ?1 AND USER_NUM = ?2", nativeQuery=true)
    int update_check(Boolean cpr, int user_num);
}
