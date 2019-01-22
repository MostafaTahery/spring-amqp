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
//@ComponentScan
public class AsyncRestController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/register", params = {"username", "password", "phonenumber", "email"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public LoggingDto register(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("phonenumber") String phonenumber, @RequestParam("email") String email, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoggingDto loggingDto = userService.register(username, password, phonenumber, email);
        return loggingDto;
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST, params = {"vcode", "username"}, produces = "application/json")
    @ResponseBody
    public LoggingDto verify(@RequestParam("vcode") String verificationCode, @RequestParam("username") String userName) throws Exception {

       LoggingDto loggingDto = userService.verify(userName, verificationCode);
        return loggingDto;
    }
}

