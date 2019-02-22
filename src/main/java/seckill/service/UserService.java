package seckill.service;

import seckill.error.BusinessException;

public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
}
