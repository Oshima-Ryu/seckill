package seckill.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import seckill.controller.viewobject.UserVO;
import seckill.error.BusinessException;
import seckill.error.EmBusinessError;
import seckill.response.CommonReturnType;
import seckill.service.UserModel;
import seckill.service.impl.UserServiceImpl;

import javax.annotation.Resource;


@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController{

    @Resource
    private UserServiceImpl userService;

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id")Integer id) throws BusinessException {
        UserModel userModel = userService.getUserById(id);
        if (userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }
}
