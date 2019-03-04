package seckill.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import seckill.error.BusinessException;
import seckill.error.EmBusinessError;
import seckill.response.CommonReturnType;
import seckill.service.OrderService;
import seckill.service.model.OrderModel;
import seckill.service.model.UserModel;

import javax.annotation.Resource;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;

    @Resource
    private HttpServletRequest httpServletRequest;

    //封装下单请求
    public CommonReturnType createOrder(@RequestParam(name="itemId")Integer itemId,
                                        @RequestParam(name="amount")Integer amount) throws BusinessException {
        //获取用户登录信息
        Boolean isLogin = (Boolean)httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户未登录");
        }
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, amount);

        return CommonReturnType.create(null);
    }

}
