var request = require('request');
var express = require('express');
var router = express.Router();

// small function to parse a youtube url and extract the video id
function get_youtube_id_from_url(url) {
    var patt = new RegExp('v=([0-9a-zA-Z_-])+');
    var id = patt.exec(url);
    if (id != null && id.length > 0) {
        return id[0].substr(2);
    }
    return '';
}

// GET videos
router.get('/videos', function(req, res) {
    var db = req.db;
    db.bind('meta')
    db.bind('videos')

    // some defaults
    var limit = parseInt(req.query.limit) || 50;
    var offset = parseInt(req.query.offset) || 0;

    // get data from the local DB + quick/dirty formatting of result response
    db.videos.find().skip(offset).limit(limit).sort({created_utc: -1}).toArray(function (err, items) {
        var result = {
            'meta': {
                'limit': limit,
                'offset': offset
            },
            'objects': items
        };

        res.json(result);
    });

    // resume scrapping reddit using the after param
    db.meta.findOne({name: 'after'}, function(err, doc){
        var url = 'http://www.reddit.com/hot.json';
        if (doc != null) {
            url += '?after=' + doc.value;
        }

        // get from redit
        // TODO: build some sort of worker/job queue to do this in the background
        // instead of "piggybacking" off the API call to get the next page from
        // reddit (PS: I know how to use RabbitMQ/Celery in python, but haven't
        // found a 1:1 equivalent for node.js yet)
        request(url, function (error, response, body) {
          if (!error && response.statusCode == 200) {
            json = JSON.parse(body)
            after = json.data.after
            children = json.data.children

            // insert the API values
            for (var i = 0; i < children.length; i++) {
                // quick and dirty check to only add youtube videos to our db
                if (children[i].data.media && children[i].data.media.type == 'youtube.com') {
                    children[i].data.media.oembed.video_id = get_youtube_id_from_url(children[i].data.media.oembed.url); 
                    db.videos.insert(children[i].data, function(err, result){
                        // 
                    });
                }
            }

            // update the stored value for the after param for reddit
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