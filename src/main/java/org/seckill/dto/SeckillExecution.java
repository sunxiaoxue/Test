package org.seckill.dto;

import org.seckill.entity.SuccesKilled;
import org.seckill.enums.SeckillStatEnum;

/**封装秒杀执行后结果
 * Created by Jodie on 2017/7/23.
 */
public class SeckillExecution {
    private  long seckillId;
    //秒杀执行结果状态
    private  int state;
    //状态表示
    private String stateInfo;
    //秒杀成功对象
    private SuccesKilled succesKilled;

 /*   public SeckillExecution(long seckillId, int state, String stateInfo, SuccesKilled succesKilled) {
        this.seckillId = seckillId;
        this.state = state;
        this.stateInfo = stateInfo;
        this.succesKilled = succesKilled;
    }*/
    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccesKilled succesKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.succesKilled = succesKilled;
    }

    /*public SeckillExecution(long seckillId, int state, String stateInfo) {
        this.seckillId = seckillId;
        this.state = state;
        this.stateInfo = stateInfo;
    }*/
    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = stateInfo;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccesKilled getSuccesKilled() {
        return succesKilled;
    }

    public void setSuccesKilled(SuccesKilled succesKilled) {
        this.succesKilled = succesKilled;
    }
}
