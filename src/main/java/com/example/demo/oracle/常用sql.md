 #merge into 

merge into  member_product t1
using card_activation t2
     ON (t2.pmid=t1.pmid)
     WHEN MATCHED THEN
     UPDATE
     SET t1.start_time=t2.start_time,t1.end_time=t2.end_time,t1.create_time=t2.create_time where to_char(t1.create_time,'yyyy-mm-dd')='1970-01-01'



#row_number() over partition by 分组聚合 分组后给每个组添加序号

select a.pmid 用户唯一编码,a.create_time 创建时间,b.product_name 产品名称,c.project_name 项目名称 from (
select a.*,row_number() over(partition by a.member_name,a.tel,a.idcardnum order by a.create_time ) rank from hl_product_members_ghw a

where a.packagecode='PD018' and a.ghwstatus=0
and a.create_time>=to_date('2021-07-13 00:00:00','yyyy-mm-dd hh24:mi:ss')
and a.create_time<=to_date('2022-07-12 23:59:59','yyyy-mm-dd hh24:mi:ss')
) a left join hl_product b on a.product_id=b.id left join hl_project c on b.project_code=c.project_code
where a.rank=1


# 在线咨询
## 友邦中文111133  友邦英文  111913
select customer_name 客户姓名,created_at 对话开始时间,platform 渠道,queue_seconds 排队时间,sustain_seconds 持续时间,
resp_seconds 响应时间,queue_start_time 排队开始时间,source 来源,substr(CUSTOMER_CUSTOM_FIELDS,30,11) 手机号码,session_id 会话id from udesk_im_sub_session_log
where im_web_plugin_id='111913'
and created_at>=to_date('2022-12-01 00:00:00','yyyy-mm-dd hh24:mi:ss')
and created_at<=to_date('2022-12-31 23:59:59','yyyy-mm-dd hh24:mi:ss')






## 利宝水滴
select
customer_name  客户姓名,
to_char(created_at,'yyyy-mm-dd') 对话开始时间,
PLATFORM   渠道,
QUEUE_SECONDS  排队时间,
SUSTAIN_SECONDS  持续时间,
RESP_SECONDS	 响应时间,
to_char(QUEUE_START_TIME,'yyyy-mm-dd')  排队开始时间,
SOURCE 来源,
substr(CUSTOMER_CUSTOM_FIELDS,21,22)  保单号,
case when im_web_plugin_id='114573' then
'利宝水滴百万医疗服务'
when im_web_plugin_id='113203' then
'利宝水滴2021重疾险服务'
when im_web_plugin_id='113913' then
'利宝水滴百万医疗组合险服务'
end  产品名称,
session_id 会话ID

from udesk_im_sub_session_log
where im_web_plugin_id in ('114573','113203','113913')
and  created_at  between to_date('2021-1-1 00:00:00','yyyy-mm-dd hh24:mi:ss') and to_date('2022-06-30 23:59:59','yyyy-mm-dd hh24:mi:ss')
order by created_at desc


##查询水滴在线咨询数据
select
a.customer_name  客户姓名,
to_char(a.created_at,'yyyy-mm-dd') 对话开始时间,
a.PLATFORM   渠道,
a.QUEUE_SECONDS  排队时间,
a.SUSTAIN_SECONDS  持续时间,
a.RESP_SECONDS   响应时间,
to_char(a.QUEUE_START_TIME,'yyyy-mm-dd')  排队开始时间,
a.SOURCE 来源,
to_char(substr(a.CUSTOMER_CUSTOM_FIELDS,21))  保单号,
b.yuanmeng_product_name 产品名称,
a.session_id 会话ID

from udesk_im_sub_session_log a inner join (
select substr(udesk_url,54,6) plug_id,yuanmeng_product_no,yuanmeng_product_name from record_shuidi_product_mapping where yuanmeng_product_no in (
'I19AB7',
'I18AJ0',
'I20AI1',
'I20AH5',
'I20AB4',
'I20BA5',
'I20BE1',
'I20BO6',
'I20BK7',
'I20BR5',
'I20BE7',
'I20BN5',
'I20CC2',
'I20BE2',
'I20BE3',
'I20BS2',
'I20CE8',
'I20BE8',
'I20BE9',
'I20CH7',
'I20BI7',
'I20BX6',
'I20BE1',
'I20CH5',
'I20CH6',
'I21AS1',
'I21BH2',
'I21BZ7',
'I21AO2',
'I20CL0',
'I21BG7',
'I21BI4',
'I21AS6',
'I21AS7',
'I21BB5',
'I21AE8',
'I21BG8',
'I21BM3',
'I21BQ3',
'I21BK5',
'I20CL4',
'I21BA4',
'I21BF6',
'I21AV1',
'I21BN2',
'I20CL1',
'I21AH8',
'I21AX1',
'I21CG8',
'I21AA9',
'I21BI3',
'I20CL6',
'I21BA6',
'I21BD3',
'I20CK1',
'I20CK2',
'I21AI1',
'I21AD7',
'I22DM1',
'I22EV4',
'I22BL1',
'I22CM1',
'I22GF6',
'I22HJ0'
)
and udesk_url is not null and length(udesk_url)>40
) b on a.im_web_plugin_id=b.plug_id
where  a.created_at  between to_date('2022-1-1 00:00:00','yyyy-mm-dd hh24:mi:ss') and to_date('2022-08-31 23:59:59','yyyy-mm-dd hh24:mi:ss')
order by a.created_at desc

## 太平健康档案数据
select a.create_time 创建时间,regexp_replace(a.username,'(.){1}','*',2,1) 用户姓名,
case when a.sex = 0 then
'女'
when a.sex = 1 then
'男'
end 性别,regexp_replace(a.mobile,'(.){4}','****',4,1) 手机号码,regexp_replace(a.idcardnum,'(.){6}','******',6,1) 身份证号码,a.height 身高,a.weight 体重,a.bloodtype 血型,
a.marriage 是否已婚,a.fertilitystatus 是否生育,a.lastmenstrulperiod 上次月经时间,a.menstrualcycle 平均月经周期,a.menstrualduration 平均月经时长,a.smokinghabits 吸烟习惯,
a.smokedage 烟龄_年,a.smokednum 每天_支,a.isdietregular 饮食是否规律,a.sleepquality 睡眠质量,a.isalwaysstayup 是否经常熬夜,a.isdefecationabnormal 是否有排便异常,
a.istakedrug 是否长期服用药物,a.worksittimes 工作坐的时间,a.housework 家务劳动,a.exercisetype 锻炼方式,a.exercisetimes 每次锻炼时间,a.diseasesymoptoms 疾病及症状,
a.otherdiseasesymoptoms 现在患有其他疾病,a.takingmedicinehealthproducts 目前服用药物及保健品,a.surgerytrauma 手术或外伤,a.othersurgerytrauma 补充手术和外伤情况,
a.medicalhistory 家族病史,a.othermedicalhistory 补充家族病史,a.drugallergy 药物过敏,a.otherdrugallergy 补充药物过敏,a.foodtouchallergy 食物或接触物过敏,
a.otherfoodtouchallergy 补充十五或接触物过敏,a.drinkinghabits 饮酒习惯,a.drinkedage 酒龄_年,a.drinkedweight 每天_两,a.exercisenumweek 每周锻炼次数,
b.product_name 产品名称
from health_record a inner join activation_temp b on a.pmid=b.pmid
where b.product_id in (
'I19BS3',
'I19NO3',
'I19BS4',
'I20BD2',
'I20CG1',
'I20CG1',
'I21AE4',
'I21BL0'
)


##根据产品统计制卡量
###这种方式会把制卡量为0的数据排除掉
select a.id,a.product_code,sum(nvl(b.count,0)) from product_temp a left join card_issue b on a.product_code=b.pid
where b.delete_flag=0
group by a.id,a.product_code
order by a.id

###这种方式不会把制卡量为0的数据排除掉
select c.id,c.product_code,sum(c.count) from (
select a.id,a.product_code,b.count from product_temp a left join card_issue b on a.product_code=b.pid ) c
group by c.id,c.product_code
order by c.id



###卡激活量和未激活量
select id,product_code ,sum(case when is_active=1 then 1 when is_active=0 then 0 else 0 end),sum(case when is_active=0 then 1 when is_active=1 then 0 else 0 end) from
(
select c.id,c.product_code,d.is_active from

(select a.id,a.product_code,b.id as ciid from product_temp a left join card_issue b on a.product_code=b.pid ) c
left join card d
on c.ciid=d.ciid
)
group by id,product_code
order by id



###卡激活量和未激活量（在有效期内的）
select id,product_code ,sum(case when is_active=1 then 1 when is_active=0 then 0 else 0 end),sum(case when is_active=0 then 1 when is_active=1 then 0 else 0 end),
sum(case when is_active=0 and sysdate<enddate then 1 when is_active=1 then 0 else 0 end)
from
(
select c.id,c.product_code,d.is_active,c.enddate from

(select a.id,a.product_code,b.id as ciid,b.enddate from product_temp a left join card_issue b on a.product_code=b.pid ) c
left join card d
on c.ciid=d.ciid
)
group by id,product_code
order by id

#太平目前的产品信息
I19BS3
I19NO3
I19BS4
I20BD2
I20CG1
I21AE4
I21BL0
I21AM4
I22KE3

##健康咨询
select har.id "id",
har.sheet_id "sheetId",
har.call_id "callId",
har.advice_type "adviceType",
har.tel "tel",
cir.province "province",
cir.city "city",
u.alias "doctor",
case when har.group_code = '11' or har.group_code = '12' then
'私人医生'
else
groups.name
end "groupCode",
date_format(har.deal_time, '%Y-%m-%d %H:%i:%s') "dealTime",
dt.name "dealType",
one.name "typeOne",
two.name "typeTwo",
sex.name "sex",
har.age "age",
har.member_name "memberName",
har.member_desc "memberDesc",
har.impression "impression",
har.diagnose "diagnose",
har.suggestion "suggestion",
har.satisfaction "satisfaction",
har.product_name "productName",
case
when har.status = 0 then
'未完成'
when har.status = 1 then
'已完成'
end "status",
uu.alias "updateUser",
date_format(ifnull(cir.create_time, now()), '%Y-%m-%d %H:%i:%s') as "callInTime",
date_format(har.create_time, '%Y-%m-%d %H:%i:%s') "createTime",
har.pmid as "pmid",
har.remarks as "remarks"
from hbs_health_advice_record har
left join hbs_call_info_record cir
on cir.call_id = har.call_id
left join sys_dictionary groups
on har.group_code = groups.node_id
left join sys_dictionary one
on har.type_one = one.node_id
left join sys_dictionary two
on har.type_two = two.node_id
left join sys_dictionary sex
on har.sex = sex.node_id
left join sys_dictionary dt
on har.deal_type = dt.node_id
left join sys_user u
on har.doctor = u.username
left join sys_user uu
on har.update_user = uu.username
where 1 = 1
and u.username like '%@doctor@%'
and u.alias like '%@doctorName@%'
and har.product_name = '@productName@'
and har.group_code = '@groupCode@'
and har.tel like '%@tel@%'
and har.status = '@status@'
and har.type_one = '@typeOne@'
and har.call_id = '@callId@'
and har.satisfaction = '@satisfaction@'
and har.advice_type = '@adviceType@'
and har.appointment = '@appointment@'
and har.call_in_time >=
str_to_date('@callInTimeFrom@ 00:00:00', '%Y-%m-%d %H:%i:%s')
and har.call_in_time <=
str_to_date('@callInTimeTo@ 23:59:59', '%Y-%m-%d %H:%i:%s')
and har.deal_time >=
str_to_date('@dealTimeFrom@ 00:00:00', '%Y-%m-%d %H:%i:%s')
and har.deal_time <=
str_to_date('@dealTimeTo@ 23:59:59', '%Y-%m-%d %H:%i:%s')
order by har.create_time desc








#太平相关的plugId
--体检报告
103931
109181
109011
103931
103931
103931
103931
--家庭医生
103941
109191
109021
113933
113983
103941
111513
--专科医生
103261,103951,103961,103971,103991,104001,104011,104021,104031,104041,104051,104061,104071,104081,104091
109031,109041,109051,109061,109071,109081,109091,109101,109111,109121,109131,109141,109151,109161,109171
108851,108861,108871,108881,108891,108901,108911,108921,108931,108941,108951,108961,108971,108981,109001
113663,113683,113673,113693,113703,113713,113723,113733,113743,113753,113763,113773,113783,113793,113803
114143,114133,114123,114113,114103,114093,114083,114073,114063,114053,114043,114033,114023,114013,114003
103261,103951,103961,103971,103991,104001,104011,104021,104031,104041,104051,104061,104071,104081,104091
113073,113063,113053,113043,113033,113023,113013,113003,112993,112983,112973,112963,112953,112943,112933


###匹配特殊字符\
select parsejson(customer_custom_fields,'TextField_78451'),customer_custom_fields from udesk_im_sub_session_log where customer_custom_fields like '%TextField_78451%'
and  parsejson(customer_custom_fields,'TextField_78451') like '%\_%' escape '\'