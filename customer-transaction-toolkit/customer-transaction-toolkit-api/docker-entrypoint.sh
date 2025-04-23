#!/bin/sh

echo "PostgreSQL: $POSTGRES_HOST:$POSTGRES_PORT"
echo "RabbitMQ: $RABBITMQ_HOST:$RABBITMQ_PORT"
echo "Redis: $REDIS_HOST:$REDIS_PORT"

wait_for_port() {
  host="$1"
  port="$2"
  echo "Waiting for $host:$port..."
  while ! nc -z "$host" "$port"; do
    sleep 2
  done
  echo "$host:$port is ready!"
}

wait_for_port "$POSTGRES_HOST" "$POSTGRES_PORT"
wait_for_port "$REDIS_HOST" "$REDIS_PORT"
wait_for_port "$RABBITMQ_HOST" "$RABBITMQ_PORT"

exec java -jar /app/customer-transaction.jar