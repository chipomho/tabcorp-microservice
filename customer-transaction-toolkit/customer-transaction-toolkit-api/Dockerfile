FROM openjdk:8-jdk-alpine

WORKDIR /app

COPY build/libs/customer-transaction.jar customer-transaction.jar

EXPOSE 8080

COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

ENV R2DBC_DATASOURCE_URL=r2dbc:pool:postgresql://postgres:5432/tabcorp
ENV R2DBC_DATASOURCE_USERNAME=tabcorp
ENV R2DBC_DATASOURCE_PASSWORD=tabcorp
ENV SPRING_REDIS_HOST=redis
ENV SPRING_REDIS_PORT=6379

ENV POSTGRES_HOST=postgres
ENV POSTGRES_PORT=5432

ENV RABBITMQ_HOST=rabbitmq
ENV RABBITMQ_PORT=5672

ENV REDIS_HOST=redis
ENV REDIS_PORT=6379


ENTRYPOINT ["/docker-entrypoint.sh"]

#ENTRYPOINT ["sh","-c","java -jar customer-transaction.jar \
#--spring.r2dbc.url=${R2DBC_DATASOURCE_URL} \
#--spring.r2dbc.username=${R2DBC_DATASOURCE_USERNAME} \
#--spring.r2dbc.password=${R2DBC_DATASOURCE_PASSWORD}"]
