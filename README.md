RushEye Manager
==================

RushEye Manager is graphic interface for RushEye.


Getting Started
===============

For now, it is best to open project in IDE lika NetBeans/Eclipse and run it from there.

Usage
===============

When we start the manager we see 2 frames, main interface frame and smaller project manager frame.

For testing we should go to File->New Project->Project From Directories

and choose path to patterns and samples (at this moment, preferably those screenshots 1 and 2 from rusheye-sample-suite).

We should see generated tree of tests in project manager frame. 
In project manager frame we can filter tree and set test results(leafs) to positive or negative with buttons.

When we click on a test(leaf) we will see Double View. Menu under each picture allows us to choose betwee sample/pattern diff and focus on changes in diff.

We can change view in Views menu.

To use 'crawl' option from RushEye, we have Project->Generate Suite Descriptor

To use 'parse' from RushEye, we need to open project using File->New Project->Project From Descriptor

Then we can use Project->Generate Result Descriptor, set params and run it.
