#!/bin/bash

# 如果用的 fabric8/java-jboss-openjdk8-jdk 这个镜像，必须要以下两句；默认镜像启动的容器所属用户是jboss
mkdir -p $PWD/logs
chmod 777 $PWD/logs

# 删除容器
docker rm -f homeloanlimit &> /dev/null


if [ "$1" == "cluster" ]; then
# profile = appbricks_cluster
    echo "profile = cluster"
    dubbo_registry_address=zookeeper://107.170.250.213:2181?backup=107.170.197.191:2181,104.236.132.55:2181
    zookeeper_address=104.236.132.55:2181
    spring_datasource_url=jdbc:oracle:thin:@104.236.132.55:1521:xe
    kong_baseUrl=http://104.236.149.7:8001/
else
# default profile = aliyun_single
    echo "profile = aliyun_single"
    dubbo_registry_address=zookeeper://47.75.8.217:2181
    zookeeper_address=47.75.8.217:2181
    spring_datasource_url=jdbc:mysql://112.74.61.59:3306/appbricks
    kong_baseUrl=http://119.29.28.59:8001/
fi


version=`date "+%Y%m%d%H"`
# 启动镜像
docker run -d --restart=on-failure:5 --privileged \
    -w /home \
    -v $PWD/logs:/home/logs \
    -p 8080:8080 \
    --name homeloanlimit deep/homeloanlimit \
    java \
        -Djava.security.egd=file:/dev/./urandom \
        -Duser.timezone=Asia/Shanghai \
        -Ddubbo.monitor.protocol=registry \
        -Ddubbo.properties.file=dubbo-prod.properties \
        -Ddubbo.protocol.dubbo.port=20883 \
        -Ddubbo.registry.address=$dubbo_registry_address \
        -XX:+PrintGCDateStamps \
        -XX:+PrintGCTimeStamps \
        -XX:+PrintGCDetails \
        -XX:+HeapDumpOnOutOfMemoryError \
        -Xloggc:logs/gc_$version.log \
        -jar /home/homeloanlimit.jar \
            --spring.profiles.active=prod \
            --zookeeper.address=$zookeeper_address \
            --spring.datasource.url=$spring_datasource_url \
            --kong.baseUrl=$kong_baseUrl
