# EventShowcase
online source for my senior project: an Android app for photo sharing at events. Winner of the 2015 Travelers Companies Research Prize

Feel free to contribute!

Abstract:
In the past, photo albums acted as gateways into our past. Today, phones and tablets can capture and hold those cherished memories; except most of the images come from a single device. EventShowcase is a mobile application for Android that strives to fill the void in digitally crowd-sourcing photography at physical events. Hosts can create location and date specific events, invite guests, and contribute images within the application. App users in the vicinity of an event can take and upload photos to a central server and vote on their favorite images from other participants. After the event ends, the top images are shared in a slideshow to preserve the most captivating depiction of the occasion. The project was developed using Eclipse with Android Development Tools and tested on a Nexus 10 tablet.


Full Details:


EventShowcase was created as a way for event-goers and planners to compile and choose as a group, democratically, what the most attractive and best portrayal of the given event is in picture form. The project was motivated by the rise in photo and video sharing apps on phones and the lack of a time/location specific instance of one. EventShowcase is meant to be a simple and intuitive tool for users to enjoy and utilize on the go and not as a distraction from the actual event. The project was also motivated by the One Second Everyday video project (http://1secondeveryday.com) that demonstrates that montages of media can add significance and emotion that a singular piece of media cannot. This influenced the decision for an end of event slideshow (or showcase) that displays a collection of the best images captured at an event forever after its completion.
The app was developed for the Google Android operating system on Eclipse Integrated Development Environment with Android Developments Tools. The main classes were written in Java and called upon a MySQL Server hosted on the domain event-showcase.org. 
An Android device running Eventshowcase agrees to the permissions listed in the apps “Android Manifest” including the camera, GPS location, Internet access, writing external storage and reading contacts. These permissions allow the application to access components and services of the device not included in the application itself. By using these permissions information and processes that the app needs are available and can be called upon. 
 
The app is controlled, beyond Android Manifest, by a series of interconnected Java classes that extend Activities and Fragments. An activity is a set of guidelines that a class must follow in order to control what a user can do on the screen. A fragment is much like an Activity but can display information specific to a specific instance easily, and two fragments can be displayed within a single activity, which can be useful for multi-paned tablet applications. To navigate between screens, activities and fragments call Intents, calls to the Android system to gather and display information from another class while closing processes on the current one to preserve memory space.
Below is a diagram showing the flow of control through the app. The Java classes below dictate all of the user interactions with the application.

 When the app is opened, it calls upon the Android Manifest to see what dependencies the app requires and what versions of Android it is targeting. From there, a Splash screen appears with the logo of the app, and then a list of events is shown. The user can create an event, look at current events posts or see a slideshow of a past event. From the current media posts they can create or view media.

The application gets and posts all of its data through a server linked to a MySQL database. This system was implemented so that anyone around the world can access the events and photos associated with the app. This functionality was key for the crowd-sourcing capability of the app. 

The MySQL database is accessed by a PHP file stored on the server that collects and displays a JSON String in a text file format that can be publicly accessed. From there an AsyncTask (done in the background while the app works) reads the JSON String information into the app and converts it into the necessary variables. From there the app can use the variables. When a user wants to post to the server, an AsyncTask calls upon a PHP POST script, which gathers parameters and inserts them as values into the MySQL database.

	To fully understand the application it is necessary to see how user interactions affect the back end and controller Java classes that run the app. Below is a description of Java class roles and how they interconnect to create a simple to use experience.

The Event List Fragment displays a list of all of the events that have been created on the app. It retrieves the Event name, start date, end date, and zip code and displays each in list form to the user. When the user clicks on an event, they are taken to the Media List associated with the event. When they click on the “+” button they are taken to the Event Fragment to enter information to create an event.

The Event Fragment allows users to enter in information for a new event. The parameters required are name, start date, end date and zip code. Users can also send a pre-made email with a link to download the app to anyone in their contact list. When they have finished filling out information, they can click “Register Event!” to save all of the information onto the database for anyone to access.

The Camera Activity controls the functionality of calling the device camera, saving a bitmap, or set of numbers representing an image, of the picture that is taken or loaded. It also creates or retrieves a filename for the picture. When the user clicks the camera icon, the activity calls the Camera application. When the user clicks the gallery icon, the activity starts an intent to gather a photo from either photos or gallery from the phone, depending on the users choice.
The Upload Activity controls both the upload of the picture taken or loaded in the Camera Activity to the “uploadedimages” folder on the event-showcase.org server and associating the filename of the picture with the specific “media” object when the user clicks the cloud and up-arrow button.

 Both activities are needed to control the functionality this screen.

The Media Viewer Fragment displays an image in a larger format than media list fragment. It allows users to vote for the image or report it as inappropriate by clicking on the action bar buttons in the top right corner. By clicking vote, 1 is added to the parameter for the votes for the media object on the database. When report is clicked, a dialog asking if the user want to report the photo as inappropriate. If the user clicks yes, a notification displays on the media object on the database for an administrator to handle.

The Media List Fragment displays the specific media associated with a given event. The data is gathered by parsing the database and retrieving media objects with the corresponding event number, stored as a parameter in each object. When a user clicks on the “+” button, a new media form is created. When a user clicks on an individual media item the Media Viewer Fragment is opened. 
The Slideshow Fragment displays the top voted images after the end date of an event is passed. It only shows when an event has ended. It loads only a user given amount of images from the given event. The user can view, but not edit any of the photos that appear in 2-second increments.

All layouts were created using XML, Extensible Markup Language, which is a set of rules that allow for the placement and sizing of objects on a screen to be parameter based as well as numerical. The app will look different on a phone and tablet and utilize less or extra screen space correctly to display relevant information. All logos, colors, and fonts are stored in the apps local resources file under drawable, color, and style respectively.

