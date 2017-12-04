package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccesKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccesKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillException;
import org.seckill.exception.SeckillSloseException;
import org.seckill.service.SecckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Jodie on 2017/7/29.
 */
//@Component(所有类型的注解) @Service @Controller
    @Service
public class SecckillServiceImpl implements SecckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //注入service依赖  也可以用@Resource
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccesKilledDao succesKilledDao;
    @Autowired
    private RedisDao redisDao;
    //MD5 盐值字符串，用于混淆MD5
    private final String slat = "34hbr4h8d7c8s7cyx8jnw2jjcxi03b43hvrwvs7hescjb#$%^u2v##b";

    /**
     * 查询所有秒杀记录
     * @return
     */
    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    /**
     * 查询单个记录
     *
     * @param seckillId
     * @return
     */
    @Override
    public Seckill queryById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    /**
     * 秒杀开启时输出秒杀接口的地址，否则输出系统使用时间和秒杀时间
     *
     * @param seckillId
     * @return
     */
    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化
        //1.访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);


        if (seckill == null) {
            //2.访问数据库
            seckill = seckillDao.queryById(seckillId);
            if(seckill==null){
                return new Exposer(false, seckillId);
            }else{
                //3.放入redis
                redisDao.putSeckill(seckill);
            }
        }
        //拿到秒杀的开始时间和结束时间
        Date starTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统时间
        Date date = new Date();
        if (date.getTime() < starTime.getTime() || date.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, date.getTime(), starTime.getTime(), endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);

        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 执行秒杀操作
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillSloseException
     */
    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点；1.开发团队达成一致约定 明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他的网络操作 （redis其他的）rpc/http协议请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillSloseException {

        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存 + 记录购买行为
        Date nowTime = new Date();
        try {
            //减库存
            int upDateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (upDateCount <= 0) {
                //没有更新到记录 秒杀结束
                throw new SeckillSloseException("seckill is closed");
            } else {
                //记录购买行为
                int insertCount = succesKilledDao.insertSeckilled(seckillId, userPhone);
                //重复秒杀
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated");
                } else {
                    //秒杀成功
                    SuccesKilled succesKilled = succesKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    // return  new SeckillExecution(seckillId,1,"秒杀成功",succesKilled);
                    //使用枚举  把提示信息放在枚举里
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, succesKilled);
                }
            }
        } catch (SeckillSloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        }
         //这个异常 我们并不能很清楚到底是抛出的SeckillSloseException  还是RepeatKillException
        //所以我们要先cath出来 e1 e2
        catch (Exception e) {

            logger.error(e.getMessage(), e);
            //所有编译期异常 转化为运行期异常
            //spring 容器会给我们回滚
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }
}
