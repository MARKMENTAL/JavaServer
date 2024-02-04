# JavaServer README

This document serves as the README for the JavaServer program, a lightweight HTTP server implemented in Java. This server is capable of serving static files from the directory it's run in and can be easily deployed on any system with Java installed.

## Running JavaServer

A pre-compiled JAR file, `JavaServer.jar`, is included for easy deployment. To run the server, use the following command:

```shell
java -jar JavaServer.jar <port number>
```

Replace `<port number>` with the port you wish the server to listen on.

## Development Environment

This server was developed on **Ubuntu 23.10** running on the **s390x** architecture, emulated in QEMU. This setup demonstrates the server's compatibility with a wide range of environments, including emulated ones, showcasing its versatility and scalability.

## Features

- Serves static files from its running directory.
- Handles concurrent client connections using threading.
- Simple and lightweight design, uses only built-in Java libraries.

## Compatibility

The server is designed to run on any system with Java installed, making it widely compatible across different platforms. Whether you're using a traditional x86 setup or exploring the capabilities of s390x mainframes emulated in QEMU, JavaServer offers a reliable solution for your HTTP serving needs.

