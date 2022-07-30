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
