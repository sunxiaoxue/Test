package org.seckill.exception;

/**重复秒杀异常（运行异常）
 * Created by Jodie on 2017/7/23.
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
