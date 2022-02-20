======================================1.日志分析常用命令

查看文件的内容
cat 显示文件内容，
cat -n 显示行号

分页显示文件
more 可以分页的展现文件内容，按Enter键显示文件下一行，按空格键便显示下一页，按F键显示下一屏内容，按B键显示上一屏内容

less提供比more更丰富的功能，支持内容查找，并且能够高亮显示

显示文件尾
tail 查看文件最后几行内容
tail -n2 显示文件最后2行
tail -n2 -f 加入-f可以让tail程序不退出，并且持续显示文件新增的行

显示文件头
head 显示文件开头的一组行
head -n 指定显示文件开头的几行

内容排序
sort 用于对文件中的某列进行排序，默认是按照字符序列排列的
sort -n 按照数字从小到大排序
sort -n -r 加入-r参数，表示按照逆序排列
sort -k2 -t ' ' -n 参数-k用来指定排序的列，此处为2表示第二列。-t参数指定列分隔符，这里列的分隔符为空格

字符统计
wc 命令可以用来统计指定文件的字符数、字数、行数，并输出统计结果
wc -l 参数-l表示统计文件中的行数
wc -c 参数-c用来显示文件的字节数
wc -L 参数-L查看最长的行长度
wc -w 参数-w查看文件包含多少个单词

查看重复出现的行
uniq 显示文件中行重复的次数，或者显示仅出现一次的行，以及仅仅显示重复出现的行。
  并且uniq的去重针对的只是连续的两行，因此它常常与sort结合起来使用
sort uniqfile | uniq -c 参数-c用来在每一行最前面加上该行出现的次数
sort uniqfile | uniq -c -u 展现仅出现一次的行 参数-u指定只显示出现一次的行
sort uniqfile | uniq -c -d 展现重复出现的行 参数-d指定只显示重复出现的行

字符串查找
grep 命令可以查找文件中符合条件的字符串，如果发现文件内容符合指定查找的行，此行便会被打印出来
grep qq 此命令中qq为指定查找的字符串
grep -c qq 参数-c可以显示查找到的行数
grep的查找支持正则表达式

文件查找
find /home/long -name access.log 在/home/long中查找文件名为access.log的文件
find /home/long -name "*.txt" 查找以txt后缀结尾的文件
find . -print 递归打印当前目录的所有文件

whereis 此命令能够方便地定位到文件系统中可执行文件的位置
whereis zkCli.sh

表达式求值
expr 10 \* 3   乘法
expr 10 % 3   求余
expr 10 + 10   加法
expr index "www.qq.com" qq 查找字符串索引位置
expr length "this is a test" 计算字符串长度

归档文件
tar 用来生成归档文件，以及将归档文件展开
tar -cf aaa.tar detach tmp 将当前目录detach和tmp目录打包成aaa.tar文件，
        其中-c参数表示生成新的包，而-f参数则指定包的名称
tar -tf aaa.tar 参数-t能够列出包中文件的名称
tar -xf aaa.tar 参数-x能够对打好的包进行解压
tar -xf aaa.tar -C xxx 参数-C表示解压的目标目录

URL访问工具
curl 在命令行下通过HTTP协议访问网页文档，支持HTTP、HTTPS、FTP、FTPS、Telnet
curl www.baidu.com 发起网页请求
curl -i www.baidu.com 返回带header的文档
curl -I www.baidu.com 返回只带header的文档

查看请求访问量
HTTP flood 也称为CC攻击
访问量排名前10的IP地址：
cat access.log | cut -fl -d " " | sort | uniq -c | sort -k 1 -n -r | head -10
页面访问量排序前10的url:
cat access.log | cut -f4 -d " " | sort | uniq -c | sort -k 1 -n -r | head -10

查看最耗时的页面
cat access.log | sort -k 2 -n -r | head -10

统计404请求的占比
export total_line=`wc -l access.log | cut -f1 -d " "` &&
export not_found_line=`awk '$6=='404'{print $6}' access.log | wc -l` &&
expr $not_found_line \* 100 / $total_line
首先计算出access.log总的行数，通过export导出total_line变量，然后通过awk命令输出404请求的行，通过wc -l
统计404请求的行数，导出为not_found_line变量，最后通过expr命令，计算出not_found_line乘100除以total_line
的值，也就是404请求所占的百分比

======================================2日志分析脚本

sed编辑器
sed编辑器也称为流编辑器(stream editor)，普通的交互式编辑器如vi,可以交互地接收键盘的命令，进行插入、删除和
文本替换等操作。而流失编辑器则是在编辑数据之前，预先指定数据的编辑规则，然后按照规则将数据输出到标准输出。在流编辑器
的所有规则与输入的行匹配完毕以后，编辑器读取下一行，重复之前的规则。处理完所有数据后，流编辑器停止。因此，sed
是面向行的，并且sed并不会修改文件本身，除非使用重定向存储输出，所以sed是比较安全的。

将日志文件中的xxx替换成yahoo输出
sed 's/xxx/yahoo/' access.log | head -10 参数s表示执行的是文本替换命令
sed -n '2,6p' accesss.log 筛选日志中指定的行输出 参数-n表示只输出指定的行'2,6p'表示选择的是第二行与第六行之间的行
sed '/qq/d' access.log 根据正则表达式删除日志中指定的行 d表示执行的是文本删除命令，将包含qq的行删除
sed '=' access.log 显示文件行号 参数=命令用来显示文件行号
sed -e 'i\head' access.log | head -10 i用来在行首插入内容，i\head表示在每行的前面插入head字符串
sed -e 'a\end' access.log | head -10  a用来在行末追加内容，a\end表示在每一行的末尾追加end字符串
sed -e '/google/c\hello' access.log | head -10 对匹配的行进行替换 c命令用于对文本进行替换操作，查找/google/匹配的行，用hello对匹配的行进行替换
sed -n '1,5p;1,5=' access.log 可以将多个命令合并起来使用，使用分号分隔 此命令是两条命令，
     第一个是打印出第一行到第五行，第二条命令是将第一行到第五行每一行的行号打印出来

如果编辑命令较为复杂，也支持将文本命令定义在文件中，如在文件testsed中定义内容如下：
s/xxx/ttb/
1,6p
1,6=

通过如下命令来执行此文件
sed -n -f testsed access.log



awk程序
尽管sed编辑器能够方便动态修改文本，但是它也有局限性，它不能提供类似于编程环境的文本处理规则定义的支持。
因此，sed可能还无法支持过于个性化的文本处理需求，而这恰好是awk的长处，它能够提供一个类似于编程的开放环境，
让你能够自定义文本处理的规则，修改和重写组织文件中的内容
awk在流编辑方面比sed更为先进的是：它提供一种编程语言而不仅仅是一组文本编辑的命令，在编程语言的内部，可以定义保存
数据的变量，使用算术和字符串操作函数对数据进行运算，支持结构化编程概念，能够使用if和循环语句等。

awk '{print $1}' access.log | head -10 打印文件制定的列 print命令用来格式化输出，支持转义字符，$1表示第一列
 ，awk默认用空格将一行分割成多格列，可以使用-F来指定列的分割符

awk '/google/{print $5,$6}' access.log | head -10 查找包含/google/的行，并且打印第五、第六列

awk 'length($0)>40{print $3}' access.log | head -10 查找length大于40的行，并且打印该行的第三列 $0表示当前的行，
          lenght($0) 用来获取当前行的长度，然后通过print $3打印出第三列

awk '{line = sprintf ( "method:%s,response:%s",$3,$7 ); print line}' access.log | head -10
定义一个line变量，用于接收sprintf的输出，而sprintf用于格式化输出第三列的请求方式和第七列的响应时间

awk支持编程的方式来定义文本处理，如果程序较为复杂，可以将文本处理程序定义在文件中，
 通过-f选项来指定包含文本处理程序的脚步文件
awk -f testawk access.log | head -10



以下为awk脚本，可以将输入的日志信息的第四列，也即是页面响应时间进行累加，并且将页面的访问次数也累加起来，最后求
平均值，也就是页面的平均响应时间
map中存放的是每个url所对应的响应时间的累加值
map_time中存放的则是每个页面被访问的次数。
awk数组支持通过字符串进行索引，最后再END块中对map进行迭代，打印出每个url的平均响应时间


{
   if( map[$4] >0 ){
      map[$4]=map[$4]+ $2
      map_time[$4]=map_time[$4]+1
   } else {
      map[$4] = $2
      map_time[$4] = 1
   }
}
END{
 for(i in map) {
   print i"="map[i]/map_time[i];
 }
}

将以上awk脚本保存到testawk_script中，然后通过如下命令进行执行
awk -f testawk_script access.log

============================3shell脚本
以下脚本能够查看系统的load和磁盘占用，在load超过2或者磁盘利用超过85%的情况下报警：
#!/bin/bash
#checkangxian@gmail.com
load=`top -n 1 | sed -n '1p' | awk '{print $11}'`
load=${load%\,*}
disk_usage=`df -h | sed -n '2p' | awk '{print $(NF -1)}'`
disk_usage=${disk_usage%\%*}
overhead=`expr $load \> 2.00`
if [ $overhead -eq 1 ];then
 echo "System load is overhead"
fi
if [disk_usage -gt 85 ];then
 echo "disk is nearly full,need more disk space"
fi
exit 0
大致流程是这样的，先通过top命令取得系统的load值，-n后面的1表示只刷新一次，然后使用sed过滤出第一行，也就是包含
load信息的行。top命令会输出1分钟、5分钟、15分钟load的平均值，通过awk筛选出1分钟内的平均值load,赋值给load变量，而
${load%\,*}则是从右边开始，过滤掉不需要的逗号。接下来便是取得磁盘的利用率，通过df命令取得磁盘利用率信息，用sed
筛选包含磁盘总的利用率的第二行，通过awk命令筛选出包含磁盘利用率的列，再使用${disk_usage%\%*}过滤掉最后的百分号。
由于取到的load信息包含两位小数，因此需要使用expr $load \> 2.00命令进行比较，并且将结果赋值给overhead.
最后通过if语句判断系统是否负载过高，或者磁盘利用率占85%以上，如果达到上述上限，则打印输出相关警告信息。






grep 'anxin.save.status:2' log-info-2021-03-02* | awk -F "anxin.save.status:2-" '{ print $2 }' | awk -F '"policyNo":' '{ print $2 }' | awk -F ',' '{ print $1 }' >> policy_no2.0303




## 写入内容到文件daemon.json中
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://yq65jq7k.mirror.aliyuncs.com"]
}
EOF





























