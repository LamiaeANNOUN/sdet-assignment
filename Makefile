MVN = mvn

.PHONY: build run test coverage clean

build:
	$(MVN) clean package -DskipTests

run:
	$(MVN) spring-boot:run

run-dev:
	SPRING_PROFILES_ACTIVE=dev $(MVN) spring-boot:run

test:
	$(MVN) test

coverage:
	$(MVN) verify

clean:
	$(MVN) clean
