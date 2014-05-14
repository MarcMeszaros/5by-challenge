5by Challenge
=============


###Back-end:
- Fetch "hot" videos from Reddit (using the [Reddit API](http://www.reddit.com/dev/api#GET_hot)) and store the ids in database.
- Serve a RESTful API to retrieve the list of videos.

###Web Front-end:
- We will show list of videos retrieved from the server.
- The list items should be presented with the video info (can be obtained from [YouTube API](https://developers.google.com/youtube/v3/)) and they should have buttons to play video, go to video, delete video, or anything else you want to add. Something like following:

![image](http://i.imgur.com/SzUYuBG.png)

### Android app:
- Build a simple app using our server API which will fetch list of videos from server and show them in a ListView with Title & Thumbnail of video.
- Clicking on the ListView should take the user to new Fragment which will auto play the video inside a WebView in the app.
- Add basic controls like Play/Pause/Seek to control this WebView based player.

###Guidelines:
- Front-end code should only be in pure JavaScript (No JQuery, nor any other JS framework).
- If you prefer, you can use any CSS libraries like Bootstrap.
- The Android app doesn't have to be compatible with ancient versions of Android. Supporting 4.0 and up will be enough.

###Extra points:
- Use NodeJS based back-end.
- Use NoSQL database.
- Use LocalStorage in Web-App and Android app to save network communication each time we fetch YouTube metadata.
- [Web+ Android] Instead of showing videos in list, show them as a full screen infinite Queue where user can go through videos by swiping in Android app and can change videos by clicking Next/Prev in web app.

Good Luck!
Don't heistate to contact us if you have any doubts.
