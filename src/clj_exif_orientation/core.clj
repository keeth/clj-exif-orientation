(ns clj-exif-orientation.core
  (:import (java.io InputStream File ByteArrayInputStream FileInputStream IOException ByteArrayOutputStream)
           (org.apache.commons.imaging Imaging)
           (javax.imageio ImageIO)
           (java.awt.image BufferedImage)
           (clj_exif_orientation ScalrWrapper))
  (:require [clj-exif.core :as exif]))

(def orientation>rotations
  {2 ["FLIP_HORZ"]
   3 ["CW_180"]
   4 ["FLIP_VERT"]
   5 ["FLIP_VERT" "CW_90"]
   6 ["CW_90"]
   7 ["FLIP_HORZ" "CW_90"]
   8 ["CW_270"]})

(defn get-orientation [bytes]
  (let [metadata (try (Imaging/getMetadata bytes) (catch Exception e nil))
        orientation (when metadata (get-in (exif/read metadata) ["Root" "Orientation"]))]
    orientation))

(defn get-rotations [bytes]
  (get orientation>rotations
    (get-orientation bytes)))

(defn do-rotation [^BufferedImage image ^String rotation]
  (ScalrWrapper/rotate image rotation))

(defn to-byte-array [^BufferedImage image ^String format]
  (let [baos (ByteArrayOutputStream.)]
    (ImageIO/write image format baos)
    (.toByteArray baos)) )

(defn transform-byte-array [bytes]
  (let [readers (ImageIO/getImageReaders
                    (ImageIO/createImageInputStream
                      (ByteArrayInputStream. bytes)))
           reader (and (.hasNext readers) (.next readers))
           formatName (when reader (.getFormatName reader))
           rotations (get-rotations bytes)
           image (try (ImageIO/read(ByteArrayInputStream. bytes)) (catch IOException e nil))]
    (if (and image formatName rotations)
      (to-byte-array (reduce do-rotation image rotations) formatName)
      bytes)))
