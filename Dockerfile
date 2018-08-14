FROM java:8
WORKDIR /
ADD caliber-email-service.jar caliber-email-service.jar
EXPOSE 9001
ENV CALIBER_CONFIG_SERVER_URL=http://ec2-18-222-144-189.us-east-2.compute.amazonaws.com:8888\
        CALIBER_EUREKA_SERVER_URL=http://34.207.166.85:8761/eureka\
        CALIBER_EMAIL_IP_ADDRESS=52.91.240.90\
        CALIBER_DB_URL=jdbc:oracle:thin:@wvudatabase.czc0zuhxihvw.us-east-1.rds.amazonaws.com:1521:orcl\
        CALIBER_DB_USER=JuneBestBatch2\
        CALIBER_DB_PASS=p4ssw0rd\
        CALIBER_EMAIL_PASS=suchPASSWORD\
        CALIBER_EMAIL_USERNAME=james.mauer.golding@gmail.com
CMD [ "java", "-jar", "caliber-email-service.jar"]
