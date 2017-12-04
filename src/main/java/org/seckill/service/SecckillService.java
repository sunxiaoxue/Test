package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillException;
import org.seckill.exception.SeckillSloseException;

import java.util.List;

/**业务接口 站在使用者角度设计接口
 * 三方面：方法定义粒度
 * 参数 ，返回类型（return 类型/异常）
 * Created by Jodie on 2017/7/23.
 */
public interface SecckillService {
    /**
     * 查询所有秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个记录
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口的地址，否则输出系统使用时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
    throws SeckillException, RepeatKillException, SeckillSloseException;

}
