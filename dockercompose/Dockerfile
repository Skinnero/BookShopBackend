FROM openjdk:17.0-oraclelinux8

WORKDIR /app

COPY target/codecoolshop-*.war /

EXPOSE 8080

CMD ["sh", "-c", "java -jar /codecoolshop-*.war"]