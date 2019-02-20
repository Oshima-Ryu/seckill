package seckill.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import seckill.controller.viewobject.UserVO;
import seckill.service.UserModel;
import seckill.service.UserService;
import seckill.service.impl.UserServiceImpl;

import javax.annotation.Resource;

@Controller("user")
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserServiceImpl userService;

    @RequestMapping("/get")
    @ResponseBody
    public UserVO getUser(@RequestParam(name="id")Integer id){
        UserModel userModel = userService.getUserById(id);
        return convertFromModel(userModel);
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
