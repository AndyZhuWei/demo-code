 #merge into 

merge into  member_product t1
using card_activation t2
     ON (t2.pmid=t1.pmid)
     WHEN MATCHED THEN
     UPDATE
     SET t1.start_time=t2.start_time,t1.end_time=t2.end_time,t1.create_time=t2.create_time where to_char(t1.create_time,'yyyy-mm-dd')='1970-01-01'