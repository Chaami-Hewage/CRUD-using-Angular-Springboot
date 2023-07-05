FROM azul/zulu-openjdk:17
ADD target/teamUp.jar teamUp.jar
EXPOSE 8444
ENTRYPOINT ["java","-jar", "teamUp.jar"]
