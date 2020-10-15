package com.example.test.Respositories;

import com.example.test.Table.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;
import java.util.Date;

public interface IdentityRepository extends JpaRepository<Identity, Long> {

    @Modifying
    @Transactional
    @Query(value="INSERT INTO IDENTITY(USER_ID, NAME, BIRTHDAY, PHONE, EMAIL, PASSWORD, DOCTOR, NURSE)\n"
            + "VALUES(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)", nativeQuery=true)
    void user_create(String user_id, String name, Date birthday, String phone, String email, String password, Boolean doctor, Boolean nurse);

    @Transactional
    @Query(value="SELECT COUNT(*) FROM IDENTITY WHERE USER_ID = ?1 AND PASSWORD = ?2", nativeQuery=true)
    int check_login(String user_id, String password);

    @Transactional
    @Query(value="SELECT COUNT(*) FROM IDENTITY WHERE USER_ID = ?1", nativeQuery=true)
    int check_userid(String user_id);

    @Transactional
    @Query(value="SELECT * FROM IDENTITY WHERE USER_ID = ?1", nativeQuery=true)
    Identity check_usernum(String user_id);

    @Transactional
    @Query(value="SELECT * FROM IDENTITY WHERE USER_NUM = ?1", nativeQuery=true)
    Identity userinfo(int user_num);

    @Transactional
    @Query(value="SELECT COUNT(*) FROM IDENTITY WHERE USER_NUM = ?1 AND PASSWORD = ?2", nativeQuery=true)
    int checkPassword(int user_num, String password);

    @Modifying
    @Transactional
    @Query(value="UPDATE IDENTITY SET PASSWORD = ?2 WHERE USER_NUM = ?1", nativeQuery=true)
    void updatePassword(int user_num, String password);

    @Transactional
    @Query(value="SELECT COUNT(*) FROM IDENTITY WHERE USER_NUM = ?1 AND PASSWORD = ?2", nativeQuery=true)
    int password_update_check(int user_num, String password);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM IDENTITY WHERE USER_NUM = ?1", nativeQuery=true)
    void deleteDB(int user_num);

    @Modifying
    @Transactional
    @Query(value="UPDATE IDENTITY SET NAME=?2, BIRTHDAY=?3, PHONE=?4, EMAIL=?5, DOCTOR=?6, NURSE=?7\n"
            + "WHERE USER_NUM = ?1", nativeQuery=true)
    void update_UserInfo(int user_num, String name, Date birthday, String phone, String email, Boolean doctor, Boolean nurse);

    @Transactional
    @Query(value="SELECT COUNT(*) FROM IDENTITY\n"
            + "WHERE USER_NUM = ?1 AND NAME=?2 AND BIRTHDAY=?3 AND PHONE=?4 AND EMAIL=?5 AND DOCTOR=?6 AND NURSE=?7",
            nativeQuery=true)
    int update_check(int user_num, String name, Date birthday, String phone, String email, Boolean doctor, Boolean nurse);

}