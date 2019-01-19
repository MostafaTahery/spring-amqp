package co.nilin.springamqp.rest.async;

import co.nilin.springamqp.data.entity.User;
import co.nilin.springamqp.data.service.UserService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping(value = "/async")
@ComponentScan
public class AsyncRestController {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private UserService userService;
    @Autowired
    private Queue queue;

    @Bean
    public Queue hello() {
        return new Queue("hello", true);
    }

    @Bean
    public AsyncReceiver asyncReceiver() {
        return new AsyncReceiver();
    }

    @RequestMapping(value = "/register", params = {"username", "password", "phonenumber", "email"}, method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("phonenumber") String phonenumber, @RequestParam("email") String email) {

        String status;
        User user;

        //Validate and Send to Queue
        if (userService.isUserNameAvailableOrInactive(username) && userService.isUserPhoneAvailableOrInactive(phonenumber)) {
            user = userService.addUser(username, phonenumber, String.valueOf((Math.round(Math.random() * 10000))), password, email, new Date());
            status = "User Data Valid...";

            //sending to rabbitMQ
            try {
                template.convertAndSend(queue.getName(), user);
                status += "Sended to Queue";
            } catch (Exception ex) {
                ex.printStackTrace();
                status += "Sending Error!";
            }
        }
        else {
            status = "User Data Invalid...";
        }

        return status;
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST, params = {"vcode", "username"})
    public String verify(@RequestParam("vcode") String verificationCode, @RequestParam("username") String userName) {

        //preparing a Registration Object;
        User user = userService.findUserByUserNameAndVerificationCode(userName,verificationCode);

        //verification of sended code
        String status;
        if (user.getVerificationCode().equals(verificationCode)) {
            userService.activateUser(user);
            status = "Verified";
        } else status = "Not Verified";

        return status;
    }
}

