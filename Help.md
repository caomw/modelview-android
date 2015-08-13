# Introduction #

ModelView is a simple 3D model viewing application for Android. It loads standard 3D model files and allow you to rotate and zoom the object in space. This is by all intents and purposes a toy application. It serves no real purpose and was written as an exercise in OpenGL programming.

# Usage #

ModelView has two "activities": browse and view. The initial activity, browse, is a file browser for 3D model files. It shows files that are packaged with the application and ones that are stored in external storage, if any. To add additional files for ModelView on your external storage (SD card), create a folder named "modelview-data" in the root of your SD card, and put the files (and or additional folders) there.

Selecting a model file will start the view activity, rendering the selected model file. You can rotate the model in the X and Y directions by dragging and flinging. You can also pinch and zoom the object. Pressing "back" will return to the browse activity.

# File Formats #

ModelView reads standard .OFF and .OBJ files. Support for .OBJ is partial in that it only loads the model vertices and faces. It does not load .OBJ materials. Partial support for .OBJ materials may be added in the future.

ModelView does it's best to robustly load files of these types, but it cannot load everything. For instance, the memory of mobile devices is limited. It is common to get "out of memory" errors when attempting to load large models.

Of course there are bugs in ModelView. If you have a .OFF or .OBJ file that you feel should be loadable but is not, you can send [email](mailto:jeffrey.blattman@gmail.com) to me and attach the file and I'll take a look.

# Preferences #

  * **Render back faces:** Render the specified (front) faces, but also render an opposite face. This is necessary in some cases as certain models assume front and back face rendering, or they do not order their vertices consistently in counter-clockwise direction. If you see portions of the object disappear as it is rotated, give this a try. This will increase memory usage and will further limit the model sizes that can be loaded. You do not normally need to set this for .OBJ files.

# Other Data Files #

See the [Data](Data.md) page for information on how to obtain more data files for use with ModelView.

# Out of Memory? #

ModelView is limited by the memory of your phone. It may not be able to load all of the 3D models packaged with it, or the ones that you add.

ModelView does its best to catch "out of memory" errors and report them nicely, but it's impossible to do this 100% of the time. The application may be so strapped for memory that it can't even report the problem. In this case, you may see the view activity just stop or force close, and return to browsing.

# Kill Permission? #

See the [WhyKillPermission](WhyKillPermission.md) page to understand why this application request permission to kill background processes.