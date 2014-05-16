var express = require('express');
var path = require('path');

// Database
var mongo = require('mongoskin');
var db = mongo.db("mongodb://localhost:27017/5by-challenge", {native_parser:true});

var index = require('./routes/index');
var api_videos = require('./routes/api/videos');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(express.static(path.join(__dirname, 'public')));

// Make our db accessible to our router
app.use(function(req, res, next){
    req.db = db;
    // quick way to log a request out to the console
    console.log('[' + res.statusCode + '] ' + req.url + ' (' + req.headers['user-agent']+ ')');
    next();
});

// routes to use
app.use('/', index);
app.use('/api', api_videos);

module.exports = app;