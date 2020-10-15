package com.example.test.Controllers;

import com.example.test.Respositories.HealthRespository;
import com.example.test.Table.Health;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/healthDB")
public class HealthController {
    @Autowired
    private HealthRespository healthRespository;

    //회원가입시 회원 생성 & 정보 저장
    @RequestMapping(method = RequestMethod.POST, value = "/userCreate")
    public void user_create(HttpServletRequest request) {
        healthRespository.user_create(Integer.parseInt(request.getParameter("USER_NUM")), request.getParameter("GENDER"),
                request.getParameter("BLOOD_PRESSURE"), request.getParameter("ALLERGY"),
                request.getParameter("CHRONIC_DISEASE"), request.getParameter("PROTECTOR_PHONE"),
                request.getParameter("PROTECTOR_RELATION"), request.getParameter("BLOOD_TYPE"));
    }

    //보호자 연락처 전송
    @RequestMapping(method = RequestMethod.POST, value = "/searchProtectorPhone")
    public Map<String, String> searchProtectorPhone(HttpServletRequest request) {
        Health health = healthRespository.searchProtectorPhone(Integer.parseInt(request.getParameter("USER_NUM")));
        Map<String, String> result = new HashMap<String, String>();
        result.put("data1", health.getPROTECTOR_PHONE());
        return result;
    }

    //회원 정보 확인
    @RequestMapping(method = RequestMethod.POST, value = "/userinfo")
    public Map<String, String> userinfo(HttpServletRequest request) {
        Health health = healthRespository.userinfo(Integer.parseInt(request.getParameter("USER_NUM")));
        Map<String, String> result = new HashMap<String, String>();
        result.put("GENDER", health.getGENDER());
        result.put("CHRONIC_DISEASE", "" + health.getCHRONIC_DISEASE());
        result.put("BLOOD_TYPE", health.getBLOOD_TYPE());
        result.put("BLOOD_PRESSURE", health.getBLOOD_PRESSURE());
        result.put("ALLERGY", "" + health.getALLERGY());
        result.put("PROTECTOR_PHONE", "" + health.getPROTECTOR_PHONE());
        result.put("PROTECTOR_RELATION", "" + health.getPROTECTOR_RELATION());
        if(request.getParameter("TYPE").equals("mypage"))
            result.put("USER_NUM", "0");
        else
            result.put("USER_NUM", "1");
        return result;
    }

    //회원 정보 변경
    @RequestMapping(method = RequestMethod.POST, value = "/updateUserInfo")
    public Map<String, String> updateUserInfo(HttpServletRequest request) {
        healthRespository.update_UserInfo(Integer.parseInt(request.getParameter("USER_NUM")), request.getParameter("GENDER"),
                request.getParameter("BLOOD_PRESSURE"), request.getParameter("ALLERGY"),
                request.getParameter("CHRONIC_DISEASE"), request.getParameter("PROTECTOR_PHONE"),
                request.getParameter("PROTECTOR_RELATION"), request.getParameter("BLOOD_TYPE"));
        Map<String, String> result = new HashMap<String, String>();
        if(healthRespository.update_check(Integer.parseInt(request.getParameter("USER_NUM")), request.getParameter("GENDER"),
                request.getParameter("BLOOD_PRESSURE"), request.getParameter("ALLERGY"),
                request.getParameter("CHRONIC_DISEASE"), request.getParameter("PROTECTOR_PHONE"),
                request.getParameter("PROTECTOR_RELATION"), request.getParameter("BLOOD_TYPE")) == 1)
            result.put("data1", "right");
        else
            result.put("data1", "wrong");
        return result;
    }
}