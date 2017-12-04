package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;

/**
 * Created by Jodie on 2017/9/19.
 */
public class RedisDao {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JedisPool jedisPool;
    //用protostuff 提供的api schema
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    /*  构造方法*/
    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);

    }

    /**
     * 通过redis拿到secckill这个对象
     */
    public Seckill getSeckill(long seckliied) {
//redis的操作逻辑

        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckliied;
                //并没有实现内部序列化操作
                //get byte[]  反序列化 Object(Secckill)把一个对象转化成一个二进制数组，传到redis当中
                //采用自定义序列化
                byte[] bytes = jedis.get(key.getBytes());
                //在redis中获取到
                if (bytes != null) {
//创建空对象
                    Seckill seckill = schema.newMessage();
                    //seckill被反序列化
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 当发现缓存没有的时候，去put一个secckill
     */
    public String putSeckill(Seckill seckill) {
// set seckill  序列化  byte[]

        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,

                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));//缓存器，默认大小 对象打的时候有一个缓冲的过程
//超时缓存
                int timeout = 60 * 60;//一个小时
                //如果不成功会报错，成功会返回ok
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
