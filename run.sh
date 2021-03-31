#!/usr/bin/env bash


source ./.env
docker-compose -f profiles/minimal-Infrastructure.yml up -d && mvn spring-boot:run -s settings.xml
docker-compose -f profiles/minimal-Infrastructure.yml stop
