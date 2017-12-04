--创建数据库
CREATE DATABASE seckill;
--使用数据库
use seckill
--创建数据库表
CREATE TABLE seckill(
seckill_id int NOT NULL AUTO_INCREMENT COMMENT '商品ID',
name varchar(120) NOT NULL COMMENT '商品名称',
number int NOT NULL COMMENT '库存数量',
start_time timestamp NOT NULL COMMENT '开始时间',
end_time timestamp NOT NULL COMMENT '结束时间',
create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE =InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀数据库';

--初始化数据
insert into
seckill (name,number,start_time,end_time)
values
('10元秒杀苹果',100 ,'2017-07-18 00:00:00','2017-07-19 00:00:00'),
('20元秒杀苹果',200 ,'2017-07-18 00:00:00','2017-07-19 00:00:00'),
('30元秒杀苹果',300 ,'2017-07-18 00:00:00','2017-07-19 00:00:00'),
('40元秒杀苹果',400 ,'2017-07-18 00:00:00','2017-07-19 00:00:00');

--明细表
--用户登录 认证相关信息
CREATE table success_killed(
seckill_id int not null COMMENT '秒杀商品ID',
user_phone bigint NOT NULL COMMENT '用户手机号',
state tinyint NOT NULL DEFAULT -0 COMMENT '-1：无效 0：成功 1：已付款',
create_time timestamp NOT NULL COMMENT '创建时间',
PRIMARY KEY (seckill_id,user_phone),
KEY idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';
