#!/bin/bash
# -------------------------------------
# 用于开发和测试环境的服务启动脚本
# 用法:
#  ./service.sh 命令 [环境]
#  如
#  ./service.sh start test
#
# @author jesse.li
# @date 2016.03.31
# -------------------------------------

scriptDir=$(cd `dirname $0`; pwd)
rootDir=`dirname $scriptDir`

cd $rootDir

# 提取版本号
version=`grep -E -o '<version>([^<]*)</version>' pom.xml | head -1 | cut -c 10- | cut -d \< -f 1`
# 服务jar包路径
jarFile="target/service-${version}.jar"
# 日志文件
buildLog="logs/build.log"
# 进程ID文件
pidFile="logs/service.pid"
# 启动参数
startArgs=""
if [[ "$2" != "" ]]; then
    startArgs="--spring.profiles.active=$2"
fi
error=""

rebuild() {
    echo -n `date '+%Y-%m-%d %H:%M:%S'` "重新构建..." >> ${buildLog}
    mvn clean package -Dmaven.test.skip=true
}

start() {
    nohup java -jar $jarFile $startArgs > /dev/null 2>&1 &
    #java -jar $jarFile $startArgs
    echo $! > $pidFile
}

stop() {
    if [[ -e $pidFile ]]; then
        pid=`cat $pidFile`
        rm -f $pidFile
    else
        pid=`ps aux | grep service- | grep -v grep | awk '{print $2}' | head -1`
    fi

    if [ "$pid"x != ""x ]; then
        kill -9 $pid
    else
        error="服务不在运行状态"
        return 1
    fi
}

case $1 in
start)
    if [[ -e $pidFile ]]; then
        echo "服务正在运行中, 进程ID: " $(cat $pidFile)
        exit 1
    fi
    echo -n "正在启动 ... "
    start
    sleep 1
    echo "成功, 进程ID:" $(cat $pidFile)
    ;;
stop)
    echo -n "正在停止 ... "
    stop
    if [[ $? -gt 0 ]]; then
        echo "失败, ${error}"
    else
        echo "成功"
    fi
    ;;
restart)
    echo -n "正在重启 ... "
    stop
    sleep 1
    start
    echo "成功, 进程ID:" $(cat $pidFile)
    ;;
status)
    if [[ -e $pidFile ]]; then
        pid=$(cat $pidFile)
    else
        pid=`ps aux | grep service- | grep -v grep | awk '{print $2}' | head -1`
    fi
    if [[ -z "$pid" ]]; then
        echo "服务不在运行状态"
        exit 1
    fi
    exists=$(ps -ef | grep $pid | grep -v grep | wc -l)
    if [[ $exists -gt 0 ]]; then
        echo "服务正在运行中, 进程ID为${pid}"
    else
        echo "服务不在运行状态, 但进程ID文件存在"
    fi
    ;;
build)
    rebuild
    ;;
*)
    echo "用于开发和测试环境的服务启动脚本"
    echo "用法: "
    echo "    ./service.sh (start|stop|restart|build|status) [dev|test|prod]"
;;
esac
