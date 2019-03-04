package seckill.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import seckill.dao.OrderDOMapper;
import seckill.dataobject.OrderDO;
import seckill.error.BusinessException;
import seckill.error.EmBusinessError;
import seckill.service.ItemService;
import seckill.service.OrderService;
import seckill.service.UserService;
import seckill.service.model.ItemModel;
import seckill.service.model.OrderModel;
import seckill.service.model.UserModel;

import javax.annotation.Resource;
import java.math.BigDecimal;

public class OrderServiceImpl implements OrderService {

    @Resource
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {
        //校验下单状态，商品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if(itemModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品不存在");
        }
        UserModel userModel = userService.getUserById(userId);
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }
        if(amount <= 0 || amount > 99 ){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "数量信息不正确");
        }

        //落单减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }

        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));

        //生成交易流水号
        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        return null;
    }


//    private String generateOrderNo(){
//        //订单号有16位
//        // 前8位位时间信息，年月日
//        //中间6位为自增序列
//        //最后2位为分库分表位
//    }

    private OrderDO convertFromOrderModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        return orderDO
    }
}
