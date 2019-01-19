package co.nilin.springamqp.rest.async;

import co.nilin.springamqp.data.dto.RegisterDto;
import co.nilin.springamqp.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/async")
@ComponentScan
public class AsyncRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", params = {"username", "password", "phonenumber", "email"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public RegisterDto register(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("phonenumber") String phonenumber, @RequestParam("email") String email) {
        RegisterDto registerDto = new RegisterDto();
        try {
            registerDto = userService.register(username, password, phonenumber, email);
        } catch (Exception ex) {
            ex.printStackTrace();
            registerDto.addError("Rest Register Method Exception");
        }

        return registerDto;
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST, params = {"vcode", "username"},produces = "application/json")
    public RegisterDto verify(@RequestParam("vcode") String verificationCode, @RequestParam("username") String userName) {
        RegisterDto registerDto = new RegisterDto();
        try {
            registerDto = userService.verify(userName, verificationCode);
        } catch (Exception ex) {
            registerDto.addError("Rest Verify Method Exception");
            ex.printStackTrace();
        }
        return registerDto;
    }
}

