var express = require('express');
var router = express.Router();

// GET videos
router.get('/videos', function(req, res) {
    var db = req.db;
    db.collection('videos').find().toArray(function (err, items) {
        res.json(items);
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