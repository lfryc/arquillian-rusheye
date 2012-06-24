RushEye Manager
==================

RushEye Manager is graphic interface for RushEye.


Getting Started
===============

For now, it is best to open project in IDE lika NetBeans/Eclipse and run it from there.

Usage
===============

When we start the manager we see 2 frames, main interface frame and smaller project manager frame.

Basic stuff
-------------------

File->New Project->Project From Directories (after that choose path to patterns and samples directory).
OR
File->New Project->Project From Suite

In project manager frame we can see generated tree of tests. In this frame we can filter tree and set test results to positive or negative with buttons.
When we click on a test(leaf) we will see Double View. Menu under each picture allows us to choose betwee sample/pattern diff and focus on changes in diff.

If we loaded project from a suite, the tree generated based on the xml file. To see images, we need to set path to samples and patterns :

Project->Sources->Set patterns/samples path

Switching between views :

View->Double View/Singe View


Generating suite
-------------------

File->New Project->Project From Directories

Project->Generate suite descriptor

Set all params.

Click 'Crawl' button.

We can see xml on Manager console.

File->Save console output


Parsing suite
-------------------

File->New Project->Project From Suite

Project->Generate Result descriptor

Set all params.

Click 'Parse' button.

Parsing will start in another thread and output file will be saved.

