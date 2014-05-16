// some API key variables
var youtube_base_url = 'https://www.googleapis.com/youtube/v3/videos';
var youtube_api_key = 'AIzaSyBUHESSsR4ijJ3pFrExVePUQhTs8ulEEuk';

/**
An ajax helper method.

Options:
    url - the url to call
    type - the type of request (GET,POST,PUT,PATCH,DELETE)
    el - a DOM element to pass down to the callback functions
    save - save the response to localstorage

TODO: make it more robust and handle POST requests 
(right now it's only really designed for GET)
*/
function ajax(options, success, failure) {
    var option = options || {};
    var url = options.url || 'http://localhost:3000/api/videos';
    var type = options.type || 'GET';
    var el = options.el || null; // allow DOM elem, to pass down to callbacks
    var save = options.save || false; // save the response

    // create the xhr request
    var xmlhttp;
    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    // add the callback
    xmlhttp.onreadystatechange = function() {
        // TODO: maybe we should care about 1xx and 3xx as success? maybe in v2...
        if (xmlhttp.readyState == 4 && (xmlhttp.status >= 200 && xmlhttp.status < 300)) {
            // save to localstorage if the flag is set
            if (save) {
                localStorage.setItem(url, xmlhttp.response);
            }

            // execute the success callback if set
            if (typeof success === 'function') {
                success(xmlhttp.response, el);
            }
        } else {
            // execute the failure callback if set
            if (typeof failure === 'function') {
                failure(xmlhttp.response, el);
            }
        }
    };

    // execute the request
    xmlhttp.open(type, url, true);
    xmlhttp.send();
}

/**
Wrapper to get details from youtube. Try to hit the localstorage first, then make
an API call if required. The ajax function looks for the "save" option, and will
save the response in local storage using the url as the key.
*/
function youtube_details(video_id, options, success, failure) {
    var option = options || {};
    options.url = youtube_base_url + '?id=' + video_id + '&key=' + youtube_api_key + '%20&part=snippet,contentDetails,statistics';

    // check if we have something in the cache, or if the save flag is set to overwrite
    // TODO have some kind of time based invalidation
    if (localStorage.getItem(options.url) == null || options.save) {
        console.log('cache miss');
        options.save = true; // in case it was an actual "miss" and not overwrite

        // pass to ajax function
        ajax(options, success, failure);
    } else {
        console.log('cache hit');
        var resp = localStorage.getItem(options.url);
        success(resp, options.el);
    }
}

/**
Helper function to add a comma to numbers
*/
function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

/**
Make a human readable version of an ISO8601 UTC date string.
*/
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