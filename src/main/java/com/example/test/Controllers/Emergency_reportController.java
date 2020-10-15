package com.example.test.Controllers;

import com.example.test.Respositories.Emergency_reportResposity;
import com.example.test.Respositories.HealthRespository;
import com.example.test.Respositories.LocationRepository;
import com.example.test.Table.Emergency_report;
import com.example.test.Table.Health;
import com.example.test.Table.Location;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/emergencyReportDB")
public class Emergency_reportController {
    @Autowired
    private Emergency_reportResposity emergency_reportResposity;
    @Autowired
    private HealthRespository healthRespository;
    @Autowired
    private LocationRepository locationRepository;
    SimpleDateFormat time_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //생성 및 정보 저장
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    Map<String, String> user_create(HttpServletRequest request) {
        Date date = new Date();
        String update_time = time_format.format(date);
        emergency_reportResposity.user_create(Integer.parseInt(request.getParameter("USER_NUM")), update_time, request.getParameter("LONGITUDE"), request.getParameter("LATITUDE"));
        Emergency_report emergency_report = emergency_reportResposity.checkE_REPORT_NUM(Integer.parseInt(request.getParameter("USER_NUM")), update_time, request.getParameter("LONGITUDE"), request.getParameter("LATITUDE"));
        Map<String, String> result = new HashMap<String, String>();
        result.put("E_REPORT_NUM", "" + emergency_report.getE_REPORT_NUM());
        return result;
    }

    //reportnum 확인
    @RequestMapping(method = RequestMethod.POST, value = "/saviorCheck")
    Map<String, String> saviorCheck(HttpServletRequest request) {
        Emergency_report emergency_report = emergency_reportResposity.checkUsernum(Integer.parseInt(request.getParameter("E_REPORT_NUM")));
        Map<String, String> result = new HashMap<String, String>();
        if(emergency_report.getE_PATIENT_NUM() == emergency_report.getE_SAVIOR_NUM())
            result.put("data1", "wrong");
        else
            result.put("data1", "right");
        return result;
    }

    //구조자 매칭시 디비 업데이트
    @RequestMapping(method = RequestMethod.POST, value = "/updateSaviorNum")
    Map<String, String> updateSaviorNum(HttpServletRequest request) {
        if(request.getParameter("TYPE").equals("update"))
            emergency_reportResposity.updateSaviorNum(Integer.parseInt(request.getParameter("E_REPORT_NUM")), Integer.parseInt(request.getParameter("E_SAVIOR_NUM")));
        Emergency_report emergency_report = emergency_reportResposity.checkUsernum(Integer.parseInt(request.getParameter("E_REPORT_NUM")));
        Health health = healthRespository.userinfo(emergency_report.getE_PATIENT_NUM());
        Map<String, String> result = new HashMap<String, String>();
        result.put("GENDER", health.getGENDER());
        result.put("CHRONIC_DISEASE", "" + health.getCHRONIC_DISEASE());
        result.put("BLOOD_TYPE", health.getBLOOD_TYPE());
        result.put("BLOOD_PRESSURE", health.getBLOOD_PRESSURE());
        result.put("ALLERGY", "" + health.getALLERGY());
        result.put("PROTECTOR_PHONE", "" + health.getPROTECTOR_PHONE());
        result.put("PROTECTOR_RELATION", "" + health.getPROTECTOR_RELATION());
        System.out.println(emergency_report.getE_PATIENT_NUM());
        Location location_p = locationRepository.searchToken(emergency_report.getE_PATIENT_NUM());
        result.put("LATITUDE", location_p.getLATITUDE());
        result.put("LONGITUDE", location_p.getLONGITUDE());

        try {
            System.out.println("" + emergency_report.getE_REPORT_NUM());
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
            notification.put("title", "saviorConnect");
            notification.put("body", "구조자가 요청을 수락했습니다.");

            JSONObject message = new JSONObject();
            message.put("token", location_p.getFIREBASE_TOKEN());
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}