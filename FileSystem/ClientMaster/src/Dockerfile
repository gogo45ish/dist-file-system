FROM openjdk:17
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN javac ClientMaster.java
EXPOSE 4999
CMD ["java","ClientMaster"]