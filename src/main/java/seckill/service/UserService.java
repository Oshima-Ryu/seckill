package seckill.service;

import seckill.error.BusinessException;

public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
    UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException;
}
