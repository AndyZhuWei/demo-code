李彦亮
#etcd集群环境搭建
DevOps:运维开发一体化
BDOps:大数据运维
SECOps:安全运维

XMind
画架构图的工具：可以使用亿图

设置主机名
hostnamectl set-hostname xxxx



#leaf环境搭建
https://github.com/Meituan-Dianping/Leaf.git
步骤
1.yum install -y git
git clone https://github.com/Meituan-Dianping/Leaf.git
2.安装maven
yum install -y maven
vi /etc/maven/setting.xml  修改为阿里云镜像
3.进入Leaf中执行如下命令
mvn clean install -DskipTests
4.进入Leaf-Server
maven运行方式
mvn spring-boot:run
shell方式运行
sh deploy/run.sh
5.测试
segment
curl http://localhost:8080/api/segment/get/leaf-segment-test
snowflake
curl http://localhost:8080/api/snowflake/get/test

#zookeeper集群搭建
没有特别的
#redis集群搭建
单主机上搭建三主三从
1.首先安装一些基础软件
yum -y install tcl gcc gcc-c++
yum -y install gcc automake autoconf libtool make
2.下载redis安装包
wget http://download.redis.io/releases/redis-5.0.5.tar.gz
3.解压缩
tar -zxvf redis-5.0.5.tar.gz
4.cd redis-5.0.5
5.执行make
6.执行make install
7.创建几个目录
mkdir -p /data/redis-5.0.5/cluster/7000
mkdir -p /data/redis-5.0.5/cluster/7001
mkdir -p /data/redis-5.0.5/cluster/7002
mkdir -p /data/redis-5.0.5/cluster/7003
mkdir -p /data/redis-5.0.5/cluster/7004
mkdir -p /data/redis-5.0.5/cluster/7005

8.分别把配置文件复制到上边的几个目录中
cp /usr/local/redis-5.0.5/redis.conf /data/redis-5.0.5/cluster/7000
cp /usr/local/redis-5.0.5/redis.conf /data/redis-5.0.5/cluster/7001
cp /usr/local/redis-5.0.5/redis.conf /data/redis-5.0.5/cluster/7002
cp /usr/local/redis-5.0.5/redis.conf /data/redis-5.0.5/cluster/7003
cp /usr/local/redis-5.0.5/redis.conf /data/redis-5.0.5/cluster/7004
cp /usr/local/redis-5.0.5/redis.conf /data/redis-5.0.5/cluster/7005

9.修改各个配置文件
bind ip地址
port 7000
pidfile /var/run/redis_7000.pid
cluster-enabled yes
daemonize yes
cluster-config-file nodes-7000.conf
cluster-node-timeout 15000
appendonly yes
其余节点类似

（ansible运维工具很不错，用脚本可以自动执行这些配置）
10.启动
redis-server /data/redis-5.0.5/cluster/7000/redis.conf
redis-server /data/redis-5.0.5/cluster/7001/redis.conf
redis-server /data/redis-5.0.5/cluster/7002/redis.conf
redis-server /data/redis-5.0.5/cluster/7003/redis.conf
redis-server /data/redis-5.0.5/cluster/7004/redis.conf
redis-server /data/redis-5.0.5/cluster/7005/redis.conf

存疑
redis-cli -a Redis123 --cluster create 10.0.0.60:7000 10.0.0.60:7001 10.0.0.60:7002 10.0.0.60:7003
10.0.0.60:7004 10.0.0.60:7005 --cluster-replicas 1




#搭建ES环境P8集群架构中要求ES单节点
环境要求
JDK1.8 
1.按照ES的yum公钥
rpm --import https://artifacts.elastic.co/GPG-KEY-elasticsearch
2.建立ES的yum源
cd /etc/yum.repos.d/
vim elasticsearch.repo
```text
[elasticsearch-7.x]
name=Elasticsearch repository for 7.x packages
baseurl=https://mirror.tuna.tsinghua.edu.cn/elasticstack/7.x/yum/
gpgcheck=1
gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
enabled=1
autorefresh=1
type=rpm-md
```
yum makecache
3.安装ES
yum install -y elasticsearch
4.修改配置文件
cd /etc/elasticsearch
4.1.修改JVM内存限制
vim jvm.options  如果电脑配置够可以256M,最大512M`
4.2.修改es本身信息
vim elasticsearch.yml
```yml
cluster.name:P8
node.name:node-1
path.data：/var/lib/elasticsearch
path.logs:/var/log/elasticsearch
network.host:0.0.0.0  #通用网段
http.port:9200
cluster.initial_master_nodes:["node-1"] #只要node-1因为我们现在是单节点
```
5.需要把9200端口隐射出去
systemctl status firewalld
如果防火墙是打开状态就把相应的端口加进去，或者把防火墙停调
systemctl stop firewalld  #停调
systemctl disable firewalld  #开机不启动
6.启动
systemctl start elasticsearch
没有报错则进行验证
7.验证
7.1 第一种方式
curl http://127.0.0.1:9200/
有回显则搭建成功
7.2 第二种方式
浏览器输入：http://192.168.80.100:9200
网页可以显示出es信息的则表示成功

以上是通过在线安装的，如果是源码安装，安装过程中还需要建立用户，修改es文件的所有者为建立的用户







