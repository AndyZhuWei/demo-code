#这是根据身份证号求年龄
(YEAR(NOW())- SUBSTRING(p.idcard,7,4)) age

#这是根据身份证号求出生日期
cast(substring(p.idcard,7,8) as date) as '出生日期'


#这是根据身份证号求性别
mysql:
case if(length(idcard)=18, cast(substring(idcard,17,1) as UNSIGNED)%2, if(length(idcard)=15,cast(substring(idcard,15,1) as UNSIGNED)%2,3)) 
when 1 then '男' when 0 then '女' else '未知' end as '性别'

oracle:
select decode(mod (to_number(substr('110228197802199547' ,17, 1)),2 ),0, '女','男' ) as sex from dual ;


#脱敏处理
CONCAT(str1,str2,…)：返回结果为连接参数产生的字符串
LEFT(str,len)：返回从字符串str 开始的len 最左字符
RIGHT(str,len)：从字符串str 开始，返回最右len 字符

##mysql
中间6位脱敏(从第7位开始，替换6位)
insert(id_number,7,6,'*****')






#去重
按照phone和activity_id唯一性去重，重复的数据按照id的最大值来选择。
select * from health_assistant_customer where id in (
select max(id) from health_assistant_customer a where activity_id in (
select activity_id from red_cell_activity_config
)
GROUP BY a.phone,a.activity_id
)

#关联更新

merge into activation_temp a
using  (
select b.* from activation_temp a inner join  data_temp b
on a.pmid=b.id
where
--a.product_id='I19NO3' and
a.idcard_num<>b.idcard_num
and a.MOBILE1 is not null


)  c
on (a.pmid=c.id )
when matched then
update  set a.ADDRESS=a.USER_NAME||'-'||a.IDCARD_NUM,a.USER_NAME=c.USER_NAME,a.IDCARD_NUM=c.IDCARD_NUM  



