package seckill.controller;

import com.alibaba.druid.util.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import seckill.controller.viewobject.UserVO;
import seckill.error.BusinessException;
import seckill.error.EmBusinessError;
import seckill.response.CommonReturnType;
import seckill.service.UserModel;
import seckill.service.impl.UserServiceImpl;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController{

    @Resource
    private UserServiceImpl userService;

    @Resource
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/resgister", method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telphone")String telphone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name="age")Integer age,
                                     @RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和otpCode
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        if(com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
        }
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setEncreptPassword(this.EncodeByMD5(password));

        userService.register(userModel);
        return CommonReturnType.create("注册成功");
    }

    public String EncodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();

        //加密字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));

        return newstr;
    }

    @RequestMapping("/getotp")
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telphone")String telphone){
        //生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += randomInt;
        String otpCode = String.valueOf(randomInt);

        //将otp验证码与对应用户手机号关联
        httpServletRequest.getSession().setAttribute(telphone, otpCode);


        //将otp验证码通过短信发送给用户
        System.out.println("telphone = " + telphone + " & otpCode = " + otpCode);

        return CommonReturnType.create(null);
    }

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
