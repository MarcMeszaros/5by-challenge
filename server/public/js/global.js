function ajax(options, success, failure) {
    var option = options || {};
    var url = options.url || 'http://localhost:3000/api/videos';
    var type = options.type || 'GET';

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
        if (xmlhttp.readyState == 4 && (xmlhttp.status >= 200 && xmlhttp.status < 300)) {
            if (typeof success === 'function') {
                success(xmlhttp);
            }
        } else {
            if (typeof failure === 'function') {
                failure(xmlhttp);
            }
        }
    };

    // execute the request
    xmlhttp.open(type, url, true);
    xmlhttp.send();
}