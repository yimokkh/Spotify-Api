# Spotify API application

This repository allows you to search for data about artists, tracks and albums.

## Table of Contents

- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [Endpoints](#endpoints)

## Introduction

This is a basic REST API application built using [Spring Boot](https://spring.io/projects/spring-boot) framework and [Maven](https://maven.apache.org). The application allows the user to get information about any artists, albums and tracks of interest to them by sending a request to predefined endpoints.

## Technologies Used

- [Spring Boot](https://spring.io/projects/spring-boot): Web framework for building the REST API.
- [Spotify Web Api](https://developer.spotify.com/documentation/web-api): An external API for getting information about artists, albums, and tracks.

## Getting Started

### Prerequisites

Make sure you have the following installed:

- Java (version 21)
- Maven

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/yimokkh/Spotify-Api
    ```

The application will start on `http://localhost:8080`.

## Usage

### Endpoints

- **Getting the request data based on the name and type of the request:** 
  
  ```http
  GET /api/search?name=ARTIST(ALBUM,TRACK)_NAME&type=TYPE_OF_INPUT_VALUES
  ```

  Retrieves information about a given artist, album, or track.

  Example:
  ```http
  GET /api/search?name=егор крид&type=artist
  ```




 
