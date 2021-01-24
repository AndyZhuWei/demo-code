#这是根据身份证号求年龄
(YEAR(NOW())- SUBSTRING(p.idcard,7,4)) age

#这是根据身份证号求出生日期
cast(substring(p.idcard,7,8) as date) as '出生日期'


#这是根据身份证号求性别
case if(length(idcard)=18, cast(substring(idcard,17,1) as UNSIGNED)%2, if(length(idcard)=15,cast(substring(idcard,15,1) as UNSIGNED)%2,3)) 
when 1 then '男' when 0 then '女' else '未知' end as '性别'

#脱敏处理
CONCAT(str1,str2,…)：返回结果为连接参数产生的字符串
LEFT(str,len)：返回从字符串str 开始的len 最左字符
RIGHT(str,len)：从字符串str 开始，返回最右len 字符

