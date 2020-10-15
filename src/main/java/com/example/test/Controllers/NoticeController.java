package com.example.test.Controllers;

import com.example.test.Respositories.NoticeRespository;
import com.example.test.Table.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@RestController
@RequestMapping("/noticeDB")
public class NoticeController {
    @Autowired
    private NoticeRespository noticeRespository;

    //회원가입시 회원 생성 & 정보 저장
    @RequestMapping(method = RequestMethod.POST, value = "/userCreate")
    public Map<String, String> user_create(HttpServletRequest request) {
        noticeRespository.user_create(Integer.parseInt(request.getParameter("USER_NUM")), Boolean.parseBoolean(request.getParameter("CPR")),
                Boolean.parseBoolean(request.getParameter("ALARM")));
        Map<String, String> result = new HashMap<String, String>();
        result.put("data1", "right");
        return result;
    }

    //회원 정보 확인
    @RequestMapping(method = RequestMethod.POST, value = "/userInfo")
    public Map<String, String> userInfo(HttpServletRequest request) {
        Notice notice = noticeRespository.userInfo(Integer.parseInt(request.getParameter("USER_NUM")));
        Map<String, String> result = new HashMap<String, String>();
        result.put("CPR", "" + notice.getCPR());
        return result;
    }

    //회원 정보 업데이트 함수
    @RequestMapping(method = RequestMethod.POST, value = "/updateUserInfo")
    public Map<String, String> updateUserInfo(HttpServletRequest request) {
        noticeRespository.updateUserInfo(Boolean.parseBoolean(request.getParameter("CPR")), Integer.parseInt(request.getParameter("USER_NUM")));
        Map<String, String> result = new HashMap<String, String>();
        if(noticeRespository.update_check(Boolean.parseBoolean(request.getParameter("CPR")), Integer.parseInt(request.getParameter("USER_NUM"))) == 1)
            result.put("data1", "right");
        else
            result.put("data1", "wrong");
        return result;
    }
}
