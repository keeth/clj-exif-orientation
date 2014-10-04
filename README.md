# clj-exif-orientation

A library for transforming EXIF (typically JPEG) files to have regular, top-left orientation (Orientation 1).

A typical use-case is receiving a photo upload from a camera phone, and wanting to resize or otherwise transform
the image.  Many image processing and display tools ignore the EXIF orientation flag, resulting in an image that looks
incorrectly rotated.

[![Clojars Project](http://clojars.org/clj-exif-orientation/latest-version.svg)](http://clojars.org/clj-exif-orientation)

## License

Copyright © 2014 Keith Grennan.

Released under an MIT license.