#!/bin/bash
# -------------------------------------
# 开发服自动构建脚本, 定时从git拉取代码, 如果有代码变动则重新构建并启动服务.
#
# @author jesse.li
# @date 2016.03.31
# -------------------------------------

scriptDir=$(cd `dirname $0`; pwd)
rootDir=`dirname $scriptDir`
pullLog="${rootDir}/logs/pull.log"

cd $rootDir

reload_check() {
    n=`cat $pullLog | wc -l`
    if [ $n -gt 1 ]; then
        /bin/bash tools/service.sh build
        /bin/bash tools/service.sh restart dev
    fi
}

while :
do
    git pull > $pullLog
    reload_check
    sleep 60
done