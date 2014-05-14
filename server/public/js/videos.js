ajax({
    type: 'GET',
    url: '/api/videos'
}, function(resp){
    var json = JSON.parse(resp.response);
    console.log(json.objects[0]);
});