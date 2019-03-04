package seckill.service;

import seckill.error.BusinessException;
import seckill.service.model.ItemModel;

import java.util.List;

public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    //商品列表
    List<ItemModel> listItem();

    //商品详情
    ItemModel getItemById(Integer id);

    //库存扣减
    boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException;

}
