package co.nilin.springamqp.data.service;

import co.nilin.springamqp.data.dto.RegisterDto;
import co.nilin.springamqp.data.entity.User;

import java.util.Date;


public interface IUserService{

    public User addUser(String userName, String phoneNumber, String verificationCode, String password, String email, Date regDate);

    public User activateUser(User user);

    public Boolean isUserValidAndActive(String userName, String password);

    public Boolean isUserNameAvailableOrInactive(String userName);

    public Boolean isUserPhoneAvailableOrInactive(String phoneNumber);

    public User updateUser(User user);

    public User findUserById(Long userId);

    public User findUserByPhoneNumber(String phoneNumber);

    public User findUserByUserName(String userName);

    public User findUserByUserNameAndVerificationCode(String userName,String verificationCode);

    public RegisterDto register(String username, String password, String phonenumber, String email)throws Exception;

    public RegisterDto verify(String username, String vcode)throws Exception;

}
