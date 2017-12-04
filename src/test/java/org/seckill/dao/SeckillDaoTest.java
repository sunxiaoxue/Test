package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**配置spring和junit整合，junit启动时加载springioc容器(@RunWith(SpringJUnit4ClassRunner.class))
 * Created by Jodie on 2017/7/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
//注入dao实现类依赖
    //实现类在spring容器当中，我们可以直接使用
    //@Resource  spring容器当中去查找SeckillDao这个类型实现类，注入到单元测试类里面
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() throws Exception {
        long id=1000;
        Seckill seckill = seckillDao.queryById(id);

        System.out.println(seckill.getName());
        System.out.println(seckill);
    }
    @Test
    public void reduceNumber() throws Exception {
        Date killTime=new Date();
      /* int update= seckillDao.reduceNumber(1000L,killTime);*/
        System.out.println(seckillDao.reduceNumber(1000L, killTime));
    }
    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for(Seckill Seckill:seckills){
            System.out.println(Seckill);
        }
    }

}