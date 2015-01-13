# Cordova-Flashlight
A lightweight flashlight plugin for Cordova based on [Eddy Verbruggen](https://github.com/EddyVerbruggen/)'s [Flashlight-PhoneGap-Plugin](https://github.com/EddyVerbruggen/Flashlight-PhoneGap-Plugin).

Currently this only supports Android, but iOS and Windows Phone will be added back in later.

This project is just a test to see how much I can optimise this plugin for performance.

##Performance
First a bit of a background story! To let the torch shine, an app has to request camera access and validate the camera to see if it actually has a torch. The validation process needs to happen only once (it's not like the device is a transformer, right?) and is relative easy to process, but requesting the camera can take up to a few hundred milliseconds at most.

Eddy's plugin requests and validates the camera every time you enable or disable the torch, which is far from performant. I tore apart the code and rewrote all of it.

Now I could let this plugin check the camera upon initialisation... but why would I? This plugin is as good as stateless at the native level, meaning that you yourself have to remember the states and determine whether you need to do something or not.

When done right, this will give you a signification performance boost. To use this plugin, follow the steps below:

##Installation
```
cordova plugin add https://github.com/Skelware/Cordova-Flashlight.git
```

To remove:
```
cordova plugin remove com.skelware.plugins.flashlight
```

##Usage:
All callbacks are optional.

* 1: Either request the camera and assume it's valid, or request and validate the camera:
```javascript
window.plugins.flashlight.request(callback); //requests camera access
window.plugins.flashlight.isSupported(callback(boolean)); //requests camera access, then validates
```

* 2: Enable or disable the torch using one of the following functions:
```javascript
window.plugins.flashlight.enable(callback);
window.plugins.flashlight.disable(callback);
window.plugins.flashlight.setEnabled(boolean, callback);
```

* 3: Release the camera when the app closes, or when you don't need it anymore:
```javascript
window.plugins.flashlight.release(callback);
```

##Live Example: Fancy Flashlight
An implementation of this plugin is currently on closed beta on [Google Play](https://play.google.com/store/apps/details?id=com.skelware.flashlight), but will be public soon!

The source-code can be found in the [Fancy Flashlight Repository](https://github.com/Skelware/Fancy-Flashlight).
