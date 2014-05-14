Everything is in one repo to keep things together. Change into each subdirectory
before starting each part.

*Note: I have never built a project that used mongodb/nodejs before, 
so chances are I'm not following best practices...*

# Server
The server is responsible for getting data into the database from reddit and
to serve up the website and API.

## System Requirements
The following system requirements are assumed to be installed.

* Node.js
* MongoDB 2.6+

## Setup
1. Run ``npm install`` in the ``server`` directory
2. Run ``mkdir data`` in the ``server`` directory

## Run the Server
1. In one console, run ``mongod --dbpath data``
2. In another, run ``./bin/www``
3. Go to ``localhost:3000`` in your browser

## Caveats
- You may need to refresh the main page on the web a few times before there are
videos in the DB (see comments in code).

# Android
Android stuff here...