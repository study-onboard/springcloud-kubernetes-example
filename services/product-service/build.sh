#/bin/sh
mvn clean
mvn package -DskipTests

TAG=`date +"%Y%m%d%H%M%S"`
IMAGE="kutzhang/study-springcloud-product-service:${TAG}"
docker build . -t ${IMAGE}
docker push ${IMAGE}