FROM ubuntu:latest
LABEL authors="BAZAN"

ENTRYPOINT ["top", "-b"]