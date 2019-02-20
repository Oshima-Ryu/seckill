package seckill.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import seckill.dao.UserDOMapper;
import seckill.dao.UserPasswordDOMapper;
import seckill.dataobject.UserDO;
import seckill.dataobject.UserPasswordDO;
import seckill.service.UserModel;
import seckill.service.UserService;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

//    @Autowired
    @Resource
    private UserDOMapper userDOMapper;

    @Resource
    private UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO == null){
            return null;
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        System.out.println(userPasswordDO.getEncrptPassword());
        return convertFromDataObject(userDO, userPasswordDO);
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if (userDO == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);

        if (userPasswordDO != null){
            userModel.setEncreptPassword(userPasswordDO.getEncrptPassword());
        }

        return userModel;
    }
}
