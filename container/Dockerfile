FROM backend:base
RUN sh -c 'mkdir -p /ap/pgm'
RUN sh -c 'mkdir -p /ap/log'
ENV LOG_HOME /ap/log 
ADD *.jar /ap/pgm/
CMD ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /ap/pgm/fstop-ms-backend.jar"]

