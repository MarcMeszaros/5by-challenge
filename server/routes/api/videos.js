var request = require('request');
var express = require('express');
var router = express.Router();

// GET videos
router.get('/videos', function(req, res) {
    var db = req.db;
    db.bind('meta')
    db.bind('videos')

    // get from the local DB
    db.videos.find().toArray(function (err, items) {
        res.json(items);
    });

    // resume using the after param
    db.meta.findOne({name: 'after'}, function(err, doc){
        var url = 'http://www.reddit.com/hot.json?after=' + doc.value;

        // get from redit
        request(url, function (error, response, body) {
          if (!error && response.statusCode == 200) {
            json = JSON.parse(body)
            after = json.data.after
            children = json.data.children

            // insert the API values
            for (var i = 0; i < children.length; i++) {
                db.videos.insert(children[i].data, function(err, result){
                    // 
                });
            }

            db.meta.update({'name': 'after'}, {'name': 'after', 'value': after}, {upsert: true}, function(err, result){});
          }
        });
    });
});

// DELETE videos
router.delete('/videos/:id', function(req, res) {
    var db = req.db;
    var userToDelete = req.params.id;
    db.collection('videos').removeById(userToDelete, function(err, result) {
        res.send((result === 1) ? { msg: '' } : { msg:'error: ' + err });
    });
});

module.exports = router;