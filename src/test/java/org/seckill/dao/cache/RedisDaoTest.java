package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static java.lang.System.out;
import static java.lang.System.setOut;
import static org.junit.Assert.*;

/**
 * Created by Jodie on 2017/9/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    private long id=1000l;
    @Resource
    private RedisDao redisDao;
    @Resource
    private SeckillDao seckillDao;
    @Test
    public void getSeckill() throws Exception {

        Seckill seckill = redisDao.getSeckill(id);
        if (seckill==null){
            seckill = seckillDao.queryById(id);
            if (seckill!=null){
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                seckill = redisDao.getSeckill(id);
                System.out.println(seckill);
            }
        }

    }

}