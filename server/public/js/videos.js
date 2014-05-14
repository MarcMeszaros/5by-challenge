function create_video_dom(video) {
    console.log(video);
    var elem = document.createElement('div')
    elem.className += 'col-lg-12 video'

    // create left/right panes
    var left = document.createElement('div');
    left.className += 'col-lg-4';
    var right = document.createElement('div');
    right.className += 'col-lg-8';

    // video
    var thumbnail = document.createElement('img');
    thumbnail.width = '300';
    thumbnail.src = video.media.oembed.thumbnail_url;
    left.appendChild(thumbnail);

    // video title
    var title = document.createElement('h3');
    title.innerHTML = video.media.oembed.title;
    right.appendChild(title);

    // video description
    var description = document.createElement('p');
    description.innerHTML = video.media.oembed.description;
    right.appendChild(description);

    // assemble
    elem.appendChild(left);
    elem.appendChild(right);
    return elem;
}

// use our helper function to make an ajax call
ajax({
    type: 'GET',
    url: '/api/videos'
}, function(resp){
    var json = JSON.parse(resp.response);

    // loop through the videos, build the dom object and append
    for (var i = 0; i < json.objects.length; i++) {
        var elem = create_video_dom(json.objects[i]);
        document.getElementById('videos').appendChild(elem);
    };
});