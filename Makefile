#
# Makefile to manage the containers.
# Author: Adrian Novegil <adrian.novegil@gmail.com>
#
.DEFAULT_GOAL:=help

PROJECT_NAME=spring-petclinic-microservices
DOCKER_NETWORK=observabilitysandbox
COMPOSE_COMMAND=docker-compose --project-name=$(PROJECT_NAME)

# show some help
help:
	@echo ''
	@echo '  Usage:'
	@echo '    make <target>'
	@echo ''
	@echo '  Targets:'
	@echo '    help                  This information'
	@echo ''
	@echo '  Common Targets:'
	@echo '    create-network        Crea la red de Docker para los contenedores'
	@echo '    build                 Construye todas las imágenes a partir de los Dockerfiles'
	@echo '    run                   Arranca todos los contenedores necesarios para ejecutar el sistema'
	@echo '    up                    build + run'
	@echo '    stop                  Para los contenedores'
	@echo '    down                  Elimina los contenedores'
	@echo '    restart               stop + run'
	@echo '    status                Recupera el estado de los contenedores'
	@echo ''
	@echo '  Clean Targets:'
	@echo '    clean-network         Borra la red Docker'
	@echo '    clean-images          Borra las imágenes creadas'
	@echo ''

create-network:
ifeq ($(shell docker network ls | grep ${DOCKER_NETWORK} | wc -l),0)
	echo "Creating docker network ${DOCKER_NETWORK}"
	@docker network create ${DOCKER_NETWORK}
endif

build:
	$(COMPOSE_COMMAND) build

run: create-network
	$(COMPOSE_COMMAND) up

up: build run

stop:
	$(COMPOSE_COMMAND) stop

down:
	$(COMPOSE_COMMAND) down

restart: stop run

status:
	$(COMPOSE_COMMAND) ps

clean-network:
	@docker network rm ${DOCKER_NETWORK} | true

clean-images:
	$(COMPOSE_COMMAND) down --rmi local | true
