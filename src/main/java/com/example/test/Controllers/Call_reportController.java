package com.example.test.Controllers;

import com.example.test.Respositories.Call_reportResposity;
import com.example.test.Respositories.Emergency_reportResposity;
import com.example.test.Respositories.LocationRepository;
import com.example.test.Table.Emergency_report;
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
import java.util.*;

@RestController
@RequestMapping("/callReportDB")
public class Call_reportController {
    @Autowired
    private Call_reportResposity call_reportResposity;
    @Autowired
    private Emergency_reportResposity emergency_reportResposity;
    @Autowired
    private LocationRepository locationRepository;

    //전화 통화시 정보 저장
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    Map<String, String> user_create(HttpServletRequest request) {
        int c_caller_num, c_recipient_num;
        Emergency_report emergency_report = emergency_reportResposity.checkUsernum(Integer.parseInt(request.getParameter("E_REPORT_NUM")));
        if(emergency_report.getE_PATIENT_NUM() == Integer.parseInt(request.getParameter("USER_NUM"))) {
            c_caller_num = emergency_report.getE_PATIENT_NUM();
            c_recipient_num = emergency_report.getE_SAVIOR_NUM();
        }
        else {
            c_caller_num = emergency_report.getE_SAVIOR_NUM();
            c_recipient_num = emergency_report.getE_PATIENT_NUM();
        }
        call_reportResposity.user_create(c_caller_num, c_recipient_num, request.getParameter("STARTTIME"), request.getParameter("TIME"), emergency_report.getE_REPORT_NUM());
        Map<String, String> result = new HashMap<String, String>();
        if(call_reportResposity.checkC_REPORT_NUM(c_caller_num, c_recipient_num, request.getParameter("STARTTIME"), request.getParameter("TIME"), emergency_report.getE_REPORT_NUM()) == 1)
            result.put("data1", "right");
        else
            result.put("data1", "wrong");
        return result;
    }

    //통화 요청
    @RequestMapping(method = RequestMethod.POST, value = "/connectCall")
    Map<String, String> connectCall(HttpServletRequest request) {
        Emergency_report emergency_report = emergency_reportResposity.checkUsernum(Integer.parseInt(request.getParameter("E_REPORT_NUM")));
        Map<String, String> result = new HashMap<String, String>();
        //webrtc room id = e_report_num + e_patient_num + e_savior_num
        result.put("data1", "" + emergency_report.getE_REPORT_NUM() + emergency_report.getE_PATIENT_NUM() + emergency_report.getE_SAVIOR_NUM());
        if(request.getParameter("TYPE").equals("voice"))
            result.put("data2", "voice");
        else
            result.put("data2", "face");
        return result;
    }

    //recipient한테 통화 요청
    @RequestMapping(value="/connectRecipient", method=RequestMethod.POST, produces="text/plain;charset=UTF-8")
    public void connectRecipient(HttpServletRequest request) throws Exception {
        String type;
        Location location;
        Emergency_report emergency_report = emergency_reportResposity.checkUsernum(Integer.parseInt(request.getParameter("E_REPORT_NUM")));
        if(emergency_report.getE_PATIENT_NUM() == Integer.parseInt(request.getParameter("USER_NUM")))
            location = locationRepository.searchToken(emergency_report.getE_SAVIOR_NUM());
        else
            location = locationRepository.searchToken(emergency_report.getE_PATIENT_NUM());
        if(request.getParameter("TYPE").equals("voice"))
            type = "voice";
        else
            type = "face";
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
            //notification.put("title", type + "/100010");
            notification.put("title", type + "/" + emergency_report.getE_REPORT_NUM() + emergency_report.getE_PATIENT_NUM() + emergency_report.getE_SAVIOR_NUM());
            notification.put("body", "전화가 왔습니다");

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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
