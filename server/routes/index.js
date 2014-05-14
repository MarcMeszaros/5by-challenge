var express = require('express');
var router = express.Router();
var request = require('request');

/* GET home page. */
router.get('/', function(req, res) {
    res.render('index', { title: '5by Challenge' });

    request('http://www.reddit.com/hot.json', function (error, response, body) {
      if (!error && response.statusCode == 200) {
        json = JSON.parse(body)
        after = json.data.after
        children = json.data.children

        var db = req.db;
        for (var i = 0; i < children.length; i++) {
            db.collection('videos').insert(children[i].data, function(err, result){
                // 
            });
        }

        db.collection('meta').update({'name': 'after'}, {'name': 'after', 'value': after}, {upsert: true}, function(err, result){});
      }
    })
});

module.exports = router;