package org.seckill.exception;

/**秒杀关闭异常
 * Created by Jodie on 2017/7/23.
 */
public class SeckillSloseException extends SeckillException{

    public SeckillSloseException(String message) {
        super(message);
    }

    public SeckillSloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
