package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccesKilled;

/**
 * Created by Jodie on 2017/7/22.
 */
public interface SuccesKilledDao {
    /**
     * 插入购买明细，可过滤重复，，设计主键的时候用的是联合主键
     * @param seckilled
     * @param userPhone
     * @return  插入的行数
     */
    int insertSeckilled(@Param("seckilled") long seckilled, @Param("userPhone") long userPhone);

    /**sss
     * 根据Id查询SuccesKilled并携带秒杀商品对象实体
     * @param seckillId
     * @return
     */
    SuccesKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
