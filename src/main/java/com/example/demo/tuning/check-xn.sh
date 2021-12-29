#! /bin/bash
# 监控Java应用，每分钟检测一次，如果连续三次进程不在则启动应用，如果连续三次返回的状态码都不正常则重启应用

WebUrl=http://localhost:8082/xn/webservice/cardActivationService
process_name=xn
dir=/opt/web/tomcat7-8082-xn

start_app(){
    source /etc/profile
    ${dir}/bin/startup.sh > /dev/null
    echo "$(date +'%F %H:%M:%S') $process_name 正在启动" >> /tmp/check-${process_name}.log
    while true;do
        TomcatServiceCode=$(curl -s -o /dev/null -m 10 --connect-timeout 10 $WebUrl -w %{http_code})
        if [ $TomcatServiceCode -eq 200 ];then
            echo "$(date +'%F %H:%M:%S') $process_name 启动完成" >> /tmp/check-${process_name}.log
            break
        fi
    done
}

stop_app(){
    kill -9 $TomcatID
    wait
    sleep 3
    rm -rf ${dir}/work/*
    rm -rf ${dir}/temp/*
    echo "$(date +'%F %H:%M:%S') $process_name 已经停止" >> /tmp/check-${process_name}.log
}

check_page(){
    TomcatServiceCode=$(curl -s -o /dev/null -m 10 --connect-timeout 10 $WebUrl -w %{http_code})
    if [ $TomcatServiceCode -eq 200 ];then
        echo "$(date +'%F %H:%M:%S') 检测 $process_name 能正常访问" >> /tmp/check-${process_name}.log
        return 0
    else
        return 1
    fi
}

n=0
while true ; do
    check_page
    m=$?
    if [ $m -eq 1 ];then
        ((n++))
        echo "$(date +'%F %H:%M:%S') 检测到 $process_name 访问异常 $n 次" >> /tmp/check-${process_name}.log
        if [ $n -eq 3 ];then
            TomcatID=$(ps -ef |grep java |grep $process_name |grep -v grep |awk '{print $2}')
            if [[ $TomcatID ]];then
                stop_app
                start_app
            else
                start_app
            fi
            n=0
        fi
    else
        n=0
    fi
    sleep 60
done
