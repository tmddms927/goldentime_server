package com.example.test.Respositories;

import com.example.test.Table.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @Modifying
    @Transactional
    @Query(value="INSERT INTO LOCATION(USER_NUM) VALUES(?1)", nativeQuery=true)
    void user_create(int usernum);

    @Modifying
    @Transactional
    @Query(value="UPDATE LOCATION " +
            "SET LONGITUDE = ?1, LATITUDE = ?2, UPDATE_LOCA_T = ?4 " +
            "WHERE USER_NUM = ?3", nativeQuery=true)
    void gps_update(String location, String latitude, int user_num, String update_loca_t);

    @Modifying
    @Transactional
    @Query(value="UPDATE LOCATION SET FIREBASE_TOKEN = ?1 WHERE USER_NUM = ?2", nativeQuery=true)
    void token_update(String token, int user_num);

    @Modifying
    @Transactional
    @Query(value="UPDATE LOCATION SET FIREBASE_TOKEN = ?1 WHERE USER_NUM = ?2", nativeQuery=true)
    void token_reset(String token, int user_num);

    @Modifying
    @Transactional
    @Query(value="SELECT *, " +
            "(6371*acos(cos(radians(?2))*cos(radians(LATITUDE))*cos(radians(LONGITUDE)-radians(?1))+sin(radians(?2))*sin(radians(LATITUDE)))) " +
            "AS distance\n" +
            "FROM LOCATION\n" +
            "HAVING distance<=0.5\n" +
            "ORDER BY distance\n" +
            "LIMIT 0,500", nativeQuery=true)
    List<Location> client_search(String location, String latitude);

    @Transactional
    @Query(value="SELECT * FROM LOCATION WHERE USER_NUM = ?1", nativeQuery=true)
    Location searchToken(int user_num);
}