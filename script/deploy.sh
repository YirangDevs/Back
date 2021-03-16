#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=yirang-springboot-restfulservice

# 현재 구동중인 애플리케이션
CURRENT_PID=$(lsof -t -i:8080)

echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY

echo "> 현재 구동 중인 애플리케이션 pid 확인"

echo "> 현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 에플리케이션이 없으므로 프로세스를 죽이지 않습니다."
else
  echo "> kill - 15 $CURRENT_PID"
  kill -15 "$CURRENT_PID"
fi

while [ -n "$(lsof -t -i:8080)" ]
do
  sleep 1
done

echo "> 새 애플리케이션 배포"

JAR_NAME="yirang-0.0.1-SNAPSHOT.jar"

echo "> JAR NAME: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"

chmod +x "$JAR_NAME"
echo "> $JAR_NAME 실행"

nohup java -Dspring.config.location=classpath:/application.properties\
  -jar "$JAR_NAME" > $REPOSITORY/nohup.out 2x81 &
# -javaagent:/home/ec2-user/app/scouter/scouter/agent.java/scouter.agent.jar\
#  -Dspring.config.location=classpath:/application.properties,\
#  classpath:/application-real.properties,/home/ec2-user/app/application-oauth.properties\
#  ,/home/ec2-user/app/application-real-db.properties\
#  -Dspring.profiles.active=real \
