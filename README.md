# clj-exif-orientation

This library helps you prepare EXIF images (typically JPEGs) for processing by tools that are not EXIF-orientation
aware.

A typical use-case: your app receives photo uploads from mobile devices, and you want to resize or otherwise transform
the photos.  Your image processing tools ignore the EXIF orientation flag, resulting in images that sometimes look
sideways after processing.

[This article](http://www.daveperrett.com/articles/2012/07/28/exif-orientation-handling-is-a-ghetto/) discusses the problem in more depth.

The **without-exif** function:

* strips EXIF metadata from the image
* applies any rotations necessary to make the resulting image have correct orientation without EXIF

## Install

[![Clojars Project](http://clojars.org/clj-exif-orientation/latest-version.svg)](http://clojars.org/clj-exif-orientation)

Add this to your project.clj dependencies:

[clj-exif-orientation "0.2.1"]

## Example

    (require '[clj-exif-orientation.core :as ceo])

    (require '[clojure.java.io :as io])

    (ceo/without-exif (io/file "input.jpg") (io/file "output.jpg"))

## API

**without-exif** input-file output-file

* reads input-file, transforms image data, writes to output-file
* returns output-file

**without-exif** input-file

* reads input-file, transforms image data, writes to a temporary file (using java.io.File/createTempFile)
* returns temporary file

**without-exif** byte-array

* reads byte array, transforms image data, writes to new byte array
* returns byte array

## Exceptional conditions

If any of the following occur, without-exif will simply pass through the input you gave it, with no modifications:

* image bytes could not be parsed / unknown format
* image does not contain EXIF metadata
* image contains EXIF but is already in Orientation 1

## TODO

* more tests with different formats and orientations

## License

Copyright © 2014 Keith Grennan.

Released under an MIT license.
