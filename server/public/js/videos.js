var youtube_base_url = 'https://www.googleapis.com/youtube/v3/videos';
var youtube_api_key = 'AIzaSyBUHESSsR4ijJ3pFrExVePUQhTs8ulEEuk';

function date2human(dateStr) {
    var date = new Date(dateStr);
    var options = {
        year: "numeric", 
        month: "short",
        day: "numeric", 
        hour: "2-digit", 
        minute: "2-digit"
    };
    return date.toLocaleTimeString('en-us', options);
}

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

    var videoDetails = document.createElement('p');
    videoDetails.className = 'details';
    videoDetails.innerHTML = '<span class="glyphicon glyphicon-film"></span><span class="definition"></span>'
        + ' - <span class="glyphicon glyphicon-user"></span>'
        + ' <a target="_blank" href="' + video.media.oembed.author_url + '">' + video.media.oembed.author_name + '</a>';
    details.appendChild(videoDetails);

    // stats
    var stats = document.createElement('div');
    stats.className = 'stats';
    details.appendChild(stats);    

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
        var video_id = json.objects[i].media.oembed.video_id;
        document.getElementById('videos').appendChild(elem);
        var stats = elem.getElementsByClassName('stats')[0];
        
        // make the ajax call to get data from youtube
        ajax({
            el: stats,
            type: 'GET',
            url:  youtube_base_url + '?id=' + video_id + '&key=' + youtube_api_key + '%20&part=snippet,contentDetails,statistics'
        }, function(resp, el) {
            var json = JSON.parse(resp.response);
            
            // add the video quality
            var details = el.parentNode.getElementsByClassName('details')[0];
            var definition = details.getElementsByClassName('definition')[0];
            definition.innerHTML = ' ' + json.items[0].contentDetails.definition.toUpperCase()

            // add the stats
            var uploaded = '<p><span class="glyphicon glyphicon-cloud-upload"></span> ' + date2human(json.items[0].snippet.publishedAt) + '</p>';
            var likes = '<p>'
                + '<span class="glyphicon glyphicon-thumbs-up"></span> '
                + numberWithCommas(json.items[0].statistics.likeCount) + ' likes'
                + '&nbsp;&nbsp;'
                + '<span class="glyphicon glyphicon-thumbs-down"></span> '
                + numberWithCommas(json.items[0].statistics.dislikeCount) + ' dislikes'
                + '&nbsp;&nbsp;'
                + '<span class="glyphicon glyphicon-heart"></span> '
                + numberWithCommas(json.items[0].statistics.favoriteCount) + ' favorites'
                + '</p>';
            var views = '<p>' 
                + '<span class="glyphicon glyphicon-eye-open"></span> ' 
                + numberWithCommas(json.items[0].statistics.viewCount) + ' views'
                + '&nbsp;&nbsp;'
                + '<span class="glyphicon glyphicon glyphicon-comment"></span> '
                + numberWithCommas(json.items[0].statistics.commentCount) + ' comments'
                + '</p>';
            
            // combine the parts/update the html
            el.innerHTML = uploaded + likes + views;
        });
    };
});