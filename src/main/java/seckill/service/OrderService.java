package seckill.service;

import seckill.error.BusinessException;
import seckill.service.model.OrderModel;

public interface OrderService {
    OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException;
}
