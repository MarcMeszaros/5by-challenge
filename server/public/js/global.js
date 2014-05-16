/**
A small ajax helper method.

TODO: make it more robust and handle POST requests 
(right now it's only really designed for GET)
*/
function ajax(options, success, failure) {
    var option = options || {};
    var url = options.url || 'http://localhost:3000/api/videos';
    var type = options.type || 'GET';
    var el = options.el || null;

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
        // TODO: maybe we should care about 1xx and 3xx as success? maybe later...
        if (xmlhttp.readyState == 4 && (xmlhttp.status >= 200 && xmlhttp.status < 300)) {
            if (typeof success === 'function') {
                success(xmlhttp, el);
            }
        } else {
            if (typeof failure === 'function') {
                failure(xmlhttp, el);
            }
        }
    };

    // execute the request
    xmlhttp.open(type, url, true);
    xmlhttp.send();
}

/**
Helper function to add a comma to numbers
*/
function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}