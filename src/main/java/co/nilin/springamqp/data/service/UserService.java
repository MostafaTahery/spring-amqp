package co.nilin.springamqp.data.service;

import co.nilin.springamqp.data.dto.RegisterDto;
import co.nilin.springamqp.data.entity.User;
import co.nilin.springamqp.data.repository.UserRepository;
import co.nilin.springamqp.rest.async.AsyncReceiver;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.rmi.registry.Registry;
import java.util.Date;
import java.util.List;


@Component
public class UserService implements IUserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RabbitTemplate template;
    @Autowired
    private UserService userService;
    @Autowired
    private Queue queue;
    @Autowired
    private RegisterDto registerDto;

    @Bean
    public Queue hello() {
        return new Queue("hello", true);
    }

    @Bean
    public AsyncReceiver asyncReceiver() {
        return new AsyncReceiver();
    }


    @Override
    public User addUser(String userName, String phoneNumber, String verificationCode, String password, String email, Date regDate) {
        User user = new User();
        user.setUserName(userName);
        user.setPhoneNumber(phoneNumber);
        user.setVerificationCode(verificationCode);
        user.setPassword(password);
        user.setEmail(email);
        user.setRegDate(regDate);
        user.setActive(false);
        userRepository.save(user);
        return user;
    }

    @Override
    public User activateUser(User user) {
        user.setActive(true);
        userRepository.save(user);
        return user;
    }

    @Override
    public Boolean isUserValidAndActive(String userName, String password) {
        List<User> users = (List<User>) userRepository.findAll();
        for (User uu : users) {
            if (uu.getUserName().equals(userName) && uu.getPassword().equals(password) && uu.getActive().booleanValue() == true) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isUserNameAvailableOrInactive(String userName) {
        List<User> users = (List<User>) userRepository.findAll();
        for (User uu : users) {
            if (uu.getUserName().equals(userName) && uu.getActive().booleanValue() == true)
                return false;
        }
        return true;
    }

    @Override
    public Boolean isUserPhoneAvailableOrInactive(String phoneNumber) {
        List<User> users = (List<User>) userRepository.findAll();
        for (User uu : users) {
            if (uu.getPhoneNumber().equals(phoneNumber) && uu.getActive().booleanValue() == true)
                return false;
        }
        return true;
    }

    @Override
    public User updateUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User findUserById(Long userId) {
        List<User> users = (List<User>) userRepository.findAll();
        for (User uu : users) {
            if (uu.getUserId().equals(userId))
                return uu;
        }
        return null;
    }

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
        List<User> users = (List<User>) userRepository.findAll();
        for (User uu : users) {
            if (uu.getPhoneNumber().equals(phoneNumber) && uu.getActive().booleanValue() == true)
                return uu;
        }
        return null;
    }

    @Override
    public User findUserByUserName(String userName) {
        List<User> users = (List<User>) userRepository.findAll();
        for (User uu : users) {
            if (uu.getUserName().equals(userName) && uu.getActive().booleanValue() == true)
                return uu;
        }
        return null;
    }

    @Override
    public User findUserByUserNameAndVerificationCode(String userName, String verificationCode) {
        List<User> users = (List<User>) userRepository.findAll();
        for (User uu : users) {
            if (uu.getUserName().equals(userName) && uu.getVerificationCode().equals(verificationCode))
                return uu;
        }
        return null;
    }

    @Override
    public RegisterDto register(String username, String password, String phonenumber, String email) throws Exception {

        User user;

        //Validate and Send to Queue
        if (isUserNameAvailableOrInactive(username) && isUserPhoneAvailableOrInactive(phonenumber)) {
            user = addUser(username, phonenumber, String.valueOf((Math.round(Math.random() * 10000))), password, email, new Date());
            registerDto.addStatus("User Data is Valid...");

            //sending to rabbitMQ

            template.convertAndSend(queue.getName(), user);
            registerDto.addStatus("Sended to Queue");


        } else {
            registerDto.addStatus("User Data Invalid...");
            registerDto.addError("User Date Error!");
            throw new Exception("Input data Error");
        }
        return registerDto;
    }

    @Override
    public RegisterDto verify(String username, String vcode) throws Exception {

        //preparing a Registration Object;
        User user = findUserByUserNameAndVerificationCode(username, vcode);

        //verification of sended code
        if (user.getVerificationCode().equals(vcode)) {
            activateUser(user);
            registerDto.addStatus("User Verified...");
        } else {
            registerDto.addStatus("User Not Verified");
            registerDto.addError("code mismatch Error");
            throw new Exception("code mismatch Error");
        }
        return registerDto;
    }

}
