package com.example.test.Controllers;

import com.example.test.Respositories.LocationRepository;
import com.example.test.Table.Location;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/locationDB")
public class LocationController {
    @Autowired
    private LocationRepository locationRepository;
    SimpleDateFormat time_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //회원가입시 회원 생성 및 정보 저장
    @RequestMapping(method = RequestMethod.POST, value = "/userCreate")
    public void user_create(HttpServletRequest request) {
        locationRepository.user_create(Integer.parseInt(request.getParameter("USER_NUM")));
    }
    
    //gps 디비 업데이트 함수
    @RequestMapping(method = RequestMethod.POST, value = "/gpsUpdate")
    public void gps_update(HttpServletRequest request){
        Date date = new Date();
        String update_time = time_format.format(date);
        locationRepository.gps_update(request.getParameter("LONGITUDE"), request.getParameter("LATITUDE"), Integer.parseInt(request.getParameter("USER_NUM")), update_time);
    }

    //firebase token 디비 업데이트 함수
    @RequestMapping(method = RequestMethod.POST, value = "/tokenUpdate")
    public void token_update(HttpServletRequest request) {
        locationRepository.token_update(request.getParameter("TOKEN"), Integer.parseInt(request.getParameter("USER_NUM")));
    }

    //로그아웃 시 firebase token 리셋
    @RequestMapping(method = RequestMethod.POST, value = "/tokenReset")
    public void token_reset(HttpServletRequest request) {
        locationRepository.token_reset("", Integer.parseInt(request.getParameter("USER_NUM")));
    }

    //주변의 사용자 탐색
    @RequestMapping(method = RequestMethod.POST, value = "/searchUser")
    Map<String, String> search_user(HttpServletRequest request) {
        List<Location> loca = locationRepository.client_search(request.getParameter("LONGITUDE"), request.getParameter("LATITUDE"));
        Map<String, String> result = new HashMap<String, String>();
        String data = "";
        for (Location location : loca) {
            data = data +location.getUSER_NUM() + "/" +  location.getLONGITUDE() + "/" + location.getLATITUDE()+ "/";
        }
        result.put("data1", data);
        return result;
    }

    //fcm 알람 보내는 함수
    @RequestMapping(value="/emergency", method=RequestMethod.POST, produces="text/plain;charset=UTF-8")
    public void emergency_alarm(HttpServletRequest request) throws Exception {
        List<Location> loca = locationRepository.client_search(request.getParameter("LONGITUDE"), request.getParameter("LATITUDE"));
        for (Location location : loca) {
            if(location.getUSER_NUM() != Integer.parseInt(request.getParameter("USER_NUM"))) {
                try {
                    System.out.println("" + location.getUSER_NUM());
                    String path = "C:\\Users\\tmddm\\AndroidStudioProjects\\main\\Server\\test\\src\\main\\resources\\google\\myapplication-c7e18-firebase-adminsdk-ivixd-aa74c9c6e9.json";
                    String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
                    String[] SCOPES = {MESSAGING_SCOPE};

                    GoogleCredential googleCredential = GoogleCredential
                            .fromStream(new FileInputStream(path))
                            .createScoped(Arrays.asList(SCOPES));
                    googleCredential.refreshToken();

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("content-type", MediaType.APPLICATION_JSON_VALUE);
                    headers.add("Authorization", "Bearer " + googleCredential.getAccessToken());

                    JSONObject notification = new JSONObject();
                    notification.put("body", "500m 근방에 응급환자가 발생했습니다.");
                    notification.put("title", request.getParameter("E_REPORT_NUM"));

                    JSONObject message = new JSONObject();
                    message.put("token", location.getFIREBASE_TOKEN());
                    message.put("notification", notification);

                    JSONObject jsonParams = new JSONObject();
                    jsonParams.put("message", message);

                    HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonParams, headers);
                    RestTemplate rt = new RestTemplate();

                    ResponseEntity<String> res = rt.exchange("https://fcm.googleapis.com/v1/projects/myapplication-c7e18/messages:send"
                            , HttpMethod.POST
                            , httpEntity
                            , String.class);

                    if (res.getStatusCode() != HttpStatus.OK) {
                        System.out.println("FCM-Exception");
                        System.out.println(res.getStatusCode().toString());
                        System.out.println(res.getHeaders().toString());
                        System.out.println(res.getBody().toString());
                    } else {
                        System.out.println(res.getStatusCode().toString());
                        System.out.println(res.getHeaders().toString());
                        System.out.println(res.getBody().toLowerCase());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}