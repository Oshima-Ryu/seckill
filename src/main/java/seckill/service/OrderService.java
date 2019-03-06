package seckill.service;

import seckill.error.BusinessException;
import seckill.service.model.OrderModel;

public interface OrderService {

    //1.通过前端url上传过来的秒杀活动id，然后下单接口内校验对应id是否属于对应商品且活动已开始
    //2.直接在下单接口内判断对应的商品是否存在秒杀活动
    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
}
