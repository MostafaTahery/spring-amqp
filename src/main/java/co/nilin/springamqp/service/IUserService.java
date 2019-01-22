package co.nilin.springamqp.service;

import co.nilin.springamqp.data.dto.LoggingDto;
import co.nilin.springamqp.data.entity.User;

import java.util.Date;
import java.util.List;


public interface IUserService{

    public User addUser(String userName, String phoneNumber, String verificationCode, String password, String email, Date regDate);

    public User activateUser(User user);

    public Boolean isUserValidAndActive(String userName, String password);

    public Boolean isUserNameAvailableOrInactive(String userName);

    public Boolean isUserPhoneAvailableOrInactive(String phoneNumber);

    public User updateUser(User user);

    public User findUserById(Long userId);

    public User findUserByPhoneNumber(String phoneNumber);

    public Boolean checkInActiveUsername(String userName) throws Exception;

    public User findUserByUserNameAndVerificationCode(String userName, String verificationCode) throws Exception;

    public LoggingDto register(String username, String password, String phonenumber, String email)throws Exception;

    public LoggingDto verify(String username, String vcode)throws Exception;

}
