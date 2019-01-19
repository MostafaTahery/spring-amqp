package co.nilin.springamqp.data.service;

import co.nilin.springamqp.data.entity.User;
import co.nilin.springamqp.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;


@Component
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

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
        List<User> users= (List<User>) userRepository.findAll();
        for (User uu:users) {
            if (uu.getUserName().equals(userName)&&uu.getPassword().equals(password)&&uu.getActive().booleanValue()==true){
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isUserNameAvailableOrInactive(String userName) {
        List<User> users= (List<User>) userRepository.findAll();
        for (User uu:users) {
            if (uu.getUserName().equals(userName)&&uu.getActive().booleanValue()==true)
                return false;
        }
        return true;
    }

    @Override
    public Boolean isUserPhoneAvailableOrInactive(String phoneNumber) {
        List<User> users= (List<User>) userRepository.findAll();
        for (User uu:users) {
            if (uu.getPhoneNumber().equals(phoneNumber)&&uu.getActive().booleanValue()==true)
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
        List<User> users= (List<User>) userRepository.findAll();
        for (User uu:users) {
            if (uu.getUserId().equals(userId))
                return uu;
        }
        return null;
    }

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
        List<User> users= (List<User>) userRepository.findAll();
        for (User uu:users) {
            if (uu.getPhoneNumber().equals(phoneNumber)&&uu.getActive().booleanValue()==true)
                return uu;
        }
        return null;
    }

    @Override
    public User findUserByUserName(String userName) {
        List<User> users= (List<User>) userRepository.findAll();
        for (User uu:users) {
            if (uu.getUserName().equals(userName)&&uu.getActive().booleanValue()==true)
                return uu;
        }
        return null;
    }

    @Override
    public User findUserByUserNameAndVerificationCode(String userName, String verificationCode) {
        List<User> users= (List<User>) userRepository.findAll();
        for (User uu:users) {
            if (uu.getUserName().equals(userName)&&uu.getVerificationCode().equals(verificationCode))
                return uu;
        }
        return null;
    }

}
