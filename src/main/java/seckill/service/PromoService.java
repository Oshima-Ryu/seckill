package seckill.service;

import seckill.service.model.PromoModel;

public interface PromoService {
    PromoModel getPromoByItemId(Integer itemId);
}
