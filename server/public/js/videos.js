function create_video_dom(video) {
    console.log(video);
    var elem = document.createElement('div')
    elem.className = 'col-lg-12 video'

    // create left/right/other panes
    var left = document.createElement('div');
    left.className = 'col-lg-4';
    var right = document.createElement('div');
    right.className = 'col-lg-8';
    var details = document.createElement('div');
    details.className = 'col-lg-12';
    var controls = document.createElement('div');
    controls.className = 'col-lg-12 controls';

    // video
    var thumbnail = document.createElement('img');
    thumbnail.width = '320';
    thumbnail.src = video.media.oembed.thumbnail_url;
    left.appendChild(thumbnail);

    // video title
    var title = document.createElement('h3');
    title.innerHTML = video.media.oembed.title;
    details.appendChild(title);

    // video description
    var description = document.createElement('p');
    description.className = 'text-muted';
    description.innerHTML = video.media.oembed.description;
    details.appendChild(description);

    // control buttons
    // in this case, create the embed iframe on the fly (with autoplay), and
    // replace the thumbnail
    var play = document.createElement('button');
    play.className = 'btn btn-primary';
    play.innerHTML = '<span class="glyphicon glyphicon-play"></span> Play';
    play.onclick = function(event) {
        var iframe = document.createElement('iframe');
        iframe.width = '320';
        iframe.height = '240';
        iframe.src = '//www.youtube.com/embed/' + video.media.oembed.video_id + '?autoplay=1&fs=1';
        iframe.frameborder = 0;
        left.replaceChild(iframe, thumbnail);
    };
    controls.appendChild(play);

    var gotovid = document.createElement('a');
    gotovid.className = 'btn btn-primary';
    gotovid.role = 'button';
    gotovid.target = '_blank';
    gotovid.href = video.media.oembed.url;
    gotovid.innerHTML = '<span class="glyphicon glyphicon-play-circle"></span> Go To Video';
    controls.appendChild(gotovid);

    var deletevid = document.createElement('button');
    deletevid.className = 'btn btn-danger';
    deletevid.innerHTML = '<span class="glyphicon glyphicon-trash"></span>';
    deletevid.onclick = function(event) {
        ajax({
            type: 'DELETE',
            url: '/api/videos/'+video._id
        }, function(resp){
            console.log('deleted!');
            elem.parentNode.removeChild(elem);
        }, function(resp){
            // TODO handle error
            console.error('cannot delete!');
        });
    };
    controls.appendChild(deletevid);

    // assemble
    right.appendChild(details);
    right.appendChild(controls);
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