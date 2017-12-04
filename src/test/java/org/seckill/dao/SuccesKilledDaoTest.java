package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccesKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Jodie on 2017/7/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccesKilledDaoTest {
    @Resource
    private SuccesKilledDao succesKilledDao;
    @Test
    public void insertSeckilled() throws Exception {
Long id=1001L;
Long phone=12345678909L;
       int ins= succesKilledDao.insertSeckilled(id,phone);
        System.out.println(ins);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        Long id=1001L;
        Long phone=12345678909L;
        SuccesKilled succesKilled = succesKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println(succesKilled);
        System.out.println(succesKilled.getSeckill());
    }

}