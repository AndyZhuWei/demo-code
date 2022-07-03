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


#按部分字段进行去重
select nu,state,ftime from (
select a.sort,trim(a.sn),b.state,b.nu,c.ftime from sn_temp20220601 a
left join kuaidi100_last_result b on trim(a.sn)=b.nu
left join kuaidi100_last_result_data c on b.id=c.last_result_id
) a where (a.nu,a.ftime) in (
select b.nu,max(c.ftime) from sn_temp20220601 a
left join kuaidi100_last_result b on trim(a.sn)=b.nu
left join kuaidi100_last_result_data c on b.id=c.last_result_id
group by b.nu

)
order by sort




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




## 分组统计
select a.id 批次号,a.customer_name 机构名称,a.product_name 产品名称,a.enddate 激活有效期,
sum(case
when  b.delete_flag=0 then
1
else
0
end) 数量,
case when a.card_issue_type=2 then '通用'
when a.card_issue_type=1 then '导入'
when a.card_issue_type=0 then '手动填写'
else '异常' end  卡号生成方式 ,a.card_prefix 卡号前缀,to_char(a.startdate,'yyyy-mm-dd')||'~'||to_char(a.enddate,'yyyy-mm-dd') 卡服务期,a.create_time 制卡时间,       sum(case
when b.is_active=1 and b.delete_flag=0 then
1
else
0
end) 已激活数量
from card_issue a left join card b on a.id=b.ciid where a.product_name like '%CAR-T%'  and a.delete_flag=0 and a.card_prefix not like 'test%'
group by a.id,a.customer_name,a.product_name,a.enddate,a.card_issue_type,a.card_prefix,to_char(a.startdate,'yyyy-mm-dd')||'~'||to_char(a.enddate,'yyyy-mm-dd'),a.create_time


