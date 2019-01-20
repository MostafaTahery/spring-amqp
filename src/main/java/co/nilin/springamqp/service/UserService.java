package co.nilin.springamqp.service;

import co.nilin.springamqp.data.dto.LoggingDto;
import co.nilin.springamqp.data.entity.User;
import co.nilin.springamqp.data.repository.UserRepository;
import co.nilin.springamqp.exception.exceptions.BusinessException;
import co.nilin.springamqp.exception.exceptions.InvalidAuthecticationException;
import co.nilin.springamqp.exception.exceptions.InvalidVerificationException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
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
    public User findUserByUserNameAndVerificationCode(String userName, String verificationCode) throws Exception {
        List<User> users = new ArrayList<>();
        users = (List<User>) userRepository.findAll();

        for (User uu : users) {
            if (uu.getUserName().equals(userName) && uu.getVerificationCode().equals(verificationCode))
                return uu;
        }
        throw new BusinessException(404 , "not-found", "user with this verify code not found", new NullPointerException());

    }

    @Override
    @ExceptionHandler
    public synchronized LoggingDto register(String username, String password, String phonenumber, String email) throws Exception {

        User user;
        LoggingDto loggingDto =new LoggingDto();
        //Validate and Send to Queue
        if (isUserNameAvailableOrInactive(username) && isUserPhoneAvailableOrInactive(phonenumber)) {
            user = addUser(username, phonenumber, String.valueOf((Math.round(Math.random() * 10000))), password, email, new Date());
            loggingDto.addStatus("User Data is Valid...");

            //sending to rabbitMQ
            template.convertAndSend(queue.getName(), user);
            loggingDto.addStatus("Sended to Queue");

        } else {
            throw new BusinessException( 401, "auth-error", "User Data Error!",  new InvalidAuthecticationException());
        }
        return loggingDto;
    }

    @Override
    @ExceptionHandler
    public LoggingDto verify(String username, String vcode) throws Exception {

        LoggingDto loggingDto =new LoggingDto();
        //preparing a Registration Object;
        User user = findUserByUserNameAndVerificationCode(username, vcode);

        //verification of sended code
        if (user.getVerificationCode().equals(vcode)) {
            activateUser(user);
            loggingDto.addStatus("User Verified");
        } else {
            throw new BusinessException( 400 , "code-not-found", "Code Mismatch Error", new InvalidVerificationException());
        }

        return loggingDto;
    }

}
