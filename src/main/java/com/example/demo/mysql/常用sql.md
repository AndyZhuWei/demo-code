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


#太平重疾流转信息查询sql
select

                wsd.sheet_id as "sheetId",
              
                wsd.process_name as "processName",
             
            
                wsd.product_name as "productName",
                wsd.member_name as "leaguerName",
				wsd.patient_name as "patientName",
                province.region_name as "province",
                city.region_name as "city",
				map.service_type_name as "serviceType",
			map.service_type,
				
				wsd.hospital_name as "hospitalName",
                date_format( wsd.create_time, '%Y-%m-%d %H:%i:%s' ) as "createTime",
                dict.name as "status",
                date_format( wsd.settlement_operate_time, '%Y-%m-%d %H:%i:%s' ) as "settlementOperateTime",
               
                wsd.leaguer_id as "leaguerId",
								wsd.reserve_order_no,
								wsd.section_area,
								wao.operation_content,
								wao.process_name,
								wao.process_key,
								wao.create_time
								
            from
                ebs_ws_severe_disease wsd
                left join sys_dictionary dict on dict.node_id = wsd.status
				left join ebs_service_type_mapping map on map.service_type = wsd.service_type
				and wsd.severe_disease_type = map.parent_id
				left join ebs_region province on province.id = wsd.province
                left join ebs_region city on city.id = wsd.city
                left join sys_dictionary feeBelong on feeBelong.node_id = wsd.fee_belonging
								left join ebs_ws_severe_disease_operation wao  on wao.sheet_id = wsd.sheet_id
            where
                wsd.delete_flag = 0
								and wsd.service_type='E0713'    
			           and product_id in (
										'ff8080816df356b8016f12f9fdca2073',
'ff8080816df356b8016e169fb53d0486',
'ff8080816df356b8016f1310ceb620b8',
'ff80808172bcca0b01739e38a4512f10',
'ff808081761e47e4017665538faa0664',
'ff8080817743f2e10177f6b3078114c9',
'ff8080817bcb3121017c113c65210b75',
'ff808081785e101f0178819dde650684',
'ff80808183780c010183e49e2baa2e26'


								 )										
							  and wsd.create_time >= str_to_date( '2022-11-01 00:00:00', '%Y-%m-%d %H:%i:%s' )
                and wsd.create_time <= str_to_date( '2022-11-23 23:59:59', '%Y-%m-%d %H:%i:%s' )
              
                order by wsd.sheet_id asc,wao.create_time