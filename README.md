# YouthVibe Attendee Marker - LPU's Open GLobal fest 2020 Attendance Marker

Download from release or Drive :

[![Drive](/Screen/drive.jpg)](http://bit.ly/YvMarker3)


[![GPLv3 license](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Generic badge](https://img.shields.io/badge/Stable-YES-red.svg)](#)

[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](#)
[![forthebadge](https://forthebadge.com/images/badges/built-for-android.svg)](#)

[![forthebadge](https://forthebadge.com/images/badges/powered-by-electricity.svg)](#)
[![forthebadge](https://forthebadge.com/images/badges/ages-12.svg)](#)

* LPU's Global fest YouthVibe Official Attendance Marker 

## Features & Note :

* Scan with QR
* Manual with Hash
* Google SignIn Auth
* Auth Allowed for specific users
* Logs User info to Firestore

### Preview Sample of Firebase rules :

```
    match /keys/{keys} {
    
    allow write: if false;
    allow read: if request.auth.uid == get(/databases/$(database)/documents/keys/rules).data.admin
    
    }
    
     match /authenticated_users/{authenticated_users} {
    
    allow write: if false;
    allow read: if request.auth.uid != null
    
    }
```

#### SUPPORT THE WORK & DEV

[![GitHub stars](https://img.shields.io/github/forks/satyajiit/YouthVibe-Attendee-Marker?style=social)](https://github.com/satyajiit/YouthVibe-Attendee-Marker/network) &nbsp;
[![GitHub stars](https://img.shields.io/github/stars/satyajiit/YouthVibe-Attendee-Marker?style=social)](https://github.com/satyajiit/YouthVibe-Attendee-Marker/stargazers)
&nbsp;
[![GitHub followers](https://img.shields.io/github/followers/satyajiit?style=social&label=Follow&maxAge=2592000)](https://github.com/satyajiit?tab=followers)

<img src="/Screen/1.png" height="583" width="350" />&nbsp;&nbsp;&nbsp;&nbsp;
<img src="/Screen/2.png" height="583" width="350" />&nbsp;<br><br><br>
<img src="/Screen/3.png" height="583" width="350" />&nbsp; &nbsp;&nbsp;&nbsp;
<img src="/Screen/4.png" height="583" width="350" />&nbsp; <br><br><br>
<img src="/Screen/7.png" height="583" width="350"/>&nbsp;&nbsp;&nbsp;&nbsp;
<img src="/Screen/6.png" height="583" width="350" />&nbsp;<br><br><br>

### Code Quality : 6/10

#### Lots of Hardwork has been made on this project
[![saythanks](https://img.shields.io/badge/say-thanks-ff69b4.svg)](https://satyajiit.xyz)
[![GitHub followers](https://img.shields.io/github/followers/satyajiit?style=social&label=Follow&maxAge=2592000)](https://github.com/satyajiit?tab=followers) <br>
[![LinkedIN](/Screen/link.png)](https://www.linkedin.com/in/satyajiit/)
