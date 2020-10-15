package com.example.test.Controllers;

import com.example.test.Respositories.IdentityRepository;
import com.example.test.Table.Identity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@RestController
@RequestMapping("/identityDB")
public class IdentityController {
    @Autowired
    private IdentityRepository identityRepository;

    //회원가입시 회원 생성 및 정보 저장
    @RequestMapping(method = RequestMethod.POST, value = "/userCreate")
    public Map<String, String> user_create(HttpServletRequest request) {
        Date birthday = Date.valueOf(request.getParameter("BIRTHDAY"));
        identityRepository.user_create(request.getParameter("USER_ID"),
                request.getParameter("NAME"), birthday,
                request.getParameter("PHONE"), request.getParameter("EMAIL"),
                request.getParameter("PASSWORD"), Boolean.parseBoolean(request.getParameter("DOCTOR")),
                Boolean.parseBoolean(request.getParameter("NURSE")));

        Map<String, String> result = new HashMap<String, String>();
        Identity identity = identityRepository.check_usernum(request.getParameter("USER_ID"));
        result.put("data1", "" + identity.getUSER_NUM());
        return result;
    }

    //로그인시 아이디 패스워드 확인
    @RequestMapping(method = RequestMethod.POST, value = "/loginCheck")
    public Map<String, String> check_login(HttpServletRequest request) {
        Map<String, String> result = new HashMap<String, String>();
        if(identityRepository.check_login(request.getParameter("USER_ID"), request.getParameter("PASSWORD")) == 1) {
            result.put("data1", "right");
            Identity identity = identityRepository.check_usernum(request.getParameter("USER_ID"));
            result.put("data2", "" + identity.getUSER_NUM());
        }
        else
            result.put("data1", "wrong");
        return result;
    }

    //회원가입시 아이디 중복 확인
    @RequestMapping(method = RequestMethod.POST, value = "/useridCheck")
    public Map<String, String> check_userid(HttpServletRequest request) {
        Map<String, String> result = new HashMap<String, String>();
        if(identityRepository.check_userid(request.getParameter("USER_ID")) == 1)
            result.put("data1", "right");
        else
            result.put("data1", "wrong");
        return result;
    }

    //회원 정보 확인
    @RequestMapping(method = RequestMethod.POST, value = "/userinfo")
    public Map<String, String> userinfo(HttpServletRequest request) {
        Identity identity = identityRepository.userinfo(Integer.parseInt(request.getParameter("USER_NUM")));
        Map<String, String> result = new HashMap<String, String>();
        result.put("NAME", identity.getNAME());
        result.put("BIRTHDAY", "" + identity.getBIRTHDAY());
        result.put("PHONE", identity.getPHONE());
        result.put("EMAIL", identity.getEMAIL());
        result.put("DOCTOR", "" + identity.getDOCTOR());
        result.put("NURSE", "" + identity.getNURSE());
        return result;
    }

    //비밀번호 변경시 현재 비밀번호 확인
    @RequestMapping(method = RequestMethod.POST, value = "/checkPassword")
    public Map<String, String> checkPassword(HttpServletRequest request) {
        Map<String, String> result = new HashMap<String, String>();
        if(identityRepository.checkPassword(Integer.parseInt(request.getParameter("USER_NUM")), request.getParameter("PASSWORD")) == 1)
            result.put("data1", "right");
        else
            result.put("data1", "wrong");
        return result;
    }

    //비밀번호 변경
    @RequestMapping(method = RequestMethod.POST, value = "/updatePassword")
    public Map<String, String> updatePassword(HttpServletRequest request) {
        identityRepository.updatePassword(Integer.parseInt(request.getParameter("USER_NUM")), request.getParameter("PASSWORD"));
        Map<String, String> result = new HashMap<String, String>();
        if(identityRepository.password_update_check(Integer.parseInt(request.getParameter("USER_NUM")), request.getParameter("PASSWORD")) == 1)
            result.put("data1", "right");
        else
            result.put("data1", "wrong");
        return result;
    }

    //회원가입 중간에 나갔을 시 identity 디비 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/userDelete")
    public void userDelete(HttpServletRequest request) {
        identityRepository.deleteDB(Integer.parseInt(request.getParameter("USER_NUM")));
    }

    //회원 정보 변경
    @RequestMapping(method = RequestMethod.POST, value = "/updateUserInfo")
    public Map<String, String> updateUserInfo(HttpServletRequest request) {
        Date birthday = Date.valueOf(request.getParameter("BIRTHDAY"));
        identityRepository.update_UserInfo(Integer.parseInt(request.getParameter("USER_NUM")),
                request.getParameter("NAME"), birthday,
                request.getParameter("PHONE"), request.getParameter("EMAIL"),
                Boolean.parseBoolean(request.getParameter("DOCTOR")),
                Boolean.parseBoolean(request.getParameter("NURSE")));
        Map<String, String> result = new HashMap<String, String>();
        if(identityRepository.update_check(Integer.parseInt(request.getParameter("USER_NUM")),
                request.getParameter("NAME"), birthday,
                request.getParameter("PHONE"), request.getParameter("EMAIL"),
                Boolean.parseBoolean(request.getParameter("DOCTOR")), Boolean.parseBoolean(request.getParameter("NURSE"))) == 1)
            result.put("data1", "right");
        else
            result.put("data1", "wrong");
        return result;
    }
}