FROM openjdk:12.0.1-jdk-oraclelinux7

ADD stoker.sh /

ARG TAR_BINARY
ADD ${TAR_BINARY} /

RUN chmod +x /stoker.sh

ENTRYPOINT ["/stoker.sh"]
