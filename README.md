
# Sample Application - Made for Goldmann Sachs Test
## Nasa's Astronomy Picture of the Day Api Renderer

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

This Application allows to search the Nasa Api - https://api.nasa.gov/planetary/apod for an astronomy picture of the day. This takes 2 params 
- ✨ Api Key  ✨
- Date fotr which the image is shown

## Features
- Search for the APOD using the API
- Favourite the date for which the result is shown
- Cache the Api results in order to get a faster access
- Load the Last seen response in case of app launch or network failure
- Dark Mode and orientation support

## Tech

Following dependencies are there : 

- OkHttp - for api request
- Coroutines library for async
- gson for faster parsing and cache hanling in terms of strings
- Min SDK version is 19

## Installation
Can run in Android studio or compatible application with the respective api level

## Screenshots
![Normal View or Results view, search can be triggered by hitting the fab](https://i.ibb.co/k0x5P3x/Screenshot-2021-11-21-at-8-43-24-PM.png)
![Date Picker Search View](https://i.ibb.co/vqqCJB7/Screenshot-2021-11-21-at-9-00-42-PM.png)
![Favourites view, clicking on any item leads results viewer, opening the item](https://i.ibb.co/brDCj27/Screenshot-2021-11-21-at-8-43-55-PM.png)
