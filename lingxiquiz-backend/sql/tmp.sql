DELETE FROM user WHERE isDelete = true;
DELETE FROM app WHERE isDelete = true;
DELETE FROM question WHERE isDelete = true;
DELETE FROM scoring_result WHERE isDelete = true;
DELETE FROM user_answer WHERE isDelete = true;

SELECT @@global.time_zone, @@session.time_zone;

-- 设置全局时区
SET GLOBAL time_zone = 'UTC';

-- 设置当前会话的时区
SET time_zone = 'UTC';

SELECT NOW();
show variables like '%zone%';
select @@time_zone;
#修改mysql全局时区为北京时间
set global time_zone = '+8:00';
#修改当前会话时区
set time_zone = '+8:00';
#立即生效
flush privileges;
