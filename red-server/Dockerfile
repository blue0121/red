FROM blue/jre8
MAINTAINER zhengjin (89609719@qq.com)

ENV JAR_FILE red-server-1.0.0-bin.tar.gz

COPY target/${JAR_FILE} /root/

RUN tar -xvf ${JAR_FILE} && \
    rm -rf ${JAR_FILE}

WORKDIR /root/
EXPOSE 7903
CMD ["bin/start.sh", "startup"]