#!/usr/bin/env bash


source ./.env
docker-compose -f profiles/minimal-Infrastructure.yml up
mvn spring-boot:run -s settings.xml
