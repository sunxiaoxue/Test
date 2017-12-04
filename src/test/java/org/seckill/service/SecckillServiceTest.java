package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Jodie on 2017/7/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SecckillServiceTest {
private  final Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SecckillService secckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = secckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void queryById() throws Exception {
        long id=1000;
        Seckill queryById = secckillService.queryById(id);
        logger.info("seckill={}",queryById);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id=1000;
        Exposer exposer = secckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer);
    }

    @Test
    public void executeSeckill() throws Exception {
        long id=1000;
        long phone=12345678009l;
        String md5="6a4e986c41f71916865187ff46bd02bf";
        SeckillExecution seckillExecution = null;
        try {
            seckillExecution = secckillService.executeSeckill(id, phone, md5);
            logger.info("result={}",seckillExecution);
        } catch (RepeatKillException e) {
           logger.info(e.getMessage(),e);
        }catch (SeckillException e) {
            logger.info(e.getMessage(),e);
        }

    }
//对上面俩个方法改一下  测试代码完整逻辑 注意可重复执行
    @Test
    public void exportSeckill() throws Exception {
        long id=1003;
        Exposer exposer = secckillService.exportSeckillUrl(id);
        if (exposer.isExposed()){
            logger.info("exposer={}",exposer);
            long phone=12345678009l;
            String md5=exposer.getMd5();
            SeckillExecution seckillExecution = null;
            try {
                seckillExecution = secckillService.executeSeckill(id, phone, md5);
                logger.info("result={}",seckillExecution);
            } catch (RepeatKillException e) {
                logger.info(e.getMessage(),e);
            }catch (SeckillException e) {
                logger.info(e.getMessage(),e);
            }
        }else{
            //警告秒杀为开启
            logger.warn("exposer={}",exposer);
        }

    }
}