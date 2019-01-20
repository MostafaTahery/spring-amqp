package co.nilin.springamqp.rest.async;

import co.nilin.springamqp.data.dto.LoggingDto;
import co.nilin.springamqp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/async")
@ComponentScan
public class AsyncRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", params = {"username", "password", "phonenumber", "email"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map<String,Object> register(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("phonenumber") String phonenumber, @RequestParam("email") String email, HttpServletRequest request, HttpServletResponse response)throws Exception {
        LoggingDto loggingDto = new LoggingDto();
        Map<String,Object> map=new HashMap<>();
        loggingDto = userService.register(username, password, phonenumber, email);
        loggingDto.addStatus(request.getAttribute("javax.servlet.error.status_code").toString());
        loggingDto.addMessage(request.getAttribute("javax.servlet.error.message").toString());
        loggingDto.addError(request.getAttribute("javax.servlet.error.name").toString());
        map.put("status",request.getAttribute("javax.servlet.error.status_code"));
        map.put("message",request.getAttribute("javax.servlet.error.message"));
       // map.put("error",request.getAttribute("javax.servlet.error.name"));
        return map;
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST, params = {"vcode", "username"},produces = "application/json")
    @ResponseBody
    public LoggingDto verify(@RequestParam("vcode") String verificationCode, @RequestParam("username") String userName)throws Exception {
        LoggingDto loggingDto = new LoggingDto();
        loggingDto = userService.verify(userName, verificationCode);
        return loggingDto;
    }
}

