package org.seckill.exception;

import org.seckill.enums.SeckillStatEnum;

/**所有秒杀相关业务异常
 * Created by Jodie on 2017/7/23.
 */
public class SeckillException extends  RuntimeException{
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }

}
