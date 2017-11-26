# Features

- download all pictures in background for disk caching purpose (Environment.DIRECTORY_DOWNLOADS)
  - overall progress is displayed in a notification
  - user is notified in case of i/o errors.
- display the latest ramen pictures from instagramApi
  - use disk caching for thumbnails
  - user is notified in case of i/o errors
  - toggle bewteen list and grid layout
- pull to refresh to get the latests pictures
- show any picture in full screen mode
  - use disk caching for full size pictures
  - the background color if fetched using `Palette` on the thumbnail


# General architecture

- mvvm pattern using `Android Databinding`
- asynchronous flow is handled using `RxJava`
- simple constructor dependency injection

# Tests

All view models are unit tested using JUnit and Mockito