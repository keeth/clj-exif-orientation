(ns clj-exif-orientation.core
  (:import (java.io InputStream File ByteArrayInputStream FileInputStream IOException ByteArrayOutputStream)
           (org.apache.commons.imaging Imaging)
           (javax.imageio ImageIO)
           (java.awt.image BufferedImage)
           (clj_exif_orientation ScalrWrapper)
           (org.apache.commons.io IOUtils))
  (:require [clj-exif.core :as exif]
            [clojure.java.io :refer [file input-stream output-stream]]))

(defrecord EncodedImage [bytes image metadata format])

(def orientation>rotations
  {2 ["FLIP_HORZ"]
   3 ["CW_180"]
   4 ["FLIP_VERT"]
   5 ["FLIP_VERT" "CW_90"]
   6 ["CW_90"]
   7 ["FLIP_HORZ" "CW_90"]
   8 ["CW_270"]})

(defn bytes? [x]
  (= (Class/forName "[B")
    (.getClass x)))

(defn get-format-name [image]
  (let [readers (ImageIO/getImageReaders
                 (ImageIO/createImageInputStream
                   (input-stream image)))
       reader (and (.hasNext readers) (.next readers))
       format-name (when reader (.getFormatName reader))]
  format-name))

(defn get-metadata [bytes]
  (try (Imaging/getMetadata bytes) (catch Exception e nil)))

(defn read-image [input]
  (try (ImageIO/read(input-stream input)) (catch IOException e nil)))

(defn read-bytes [input]
  (cond
    (bytes? input) input
    (instance? File input) (IOUtils/toByteArray (input-stream input))
    :else (throw (IllegalArgumentException. (str "Illegal argument type " input)))))

(defn get-encoded-image [image]
  (let [bytes (read-bytes image)]
      (EncodedImage.
    bytes
    (read-image bytes)
    (get-metadata bytes)
    (get-format-name image))))

(defn do-rotation [^BufferedImage image ^String rotation]
  (ScalrWrapper/rotate image rotation))

(defn to-byte-array [image format]
  (if (bytes? image)
    image
    (let [baos (ByteArrayOutputStream.)]
      (ImageIO/write image format baos)
      (.toByteArray baos))))

(defn to-file [image format output]
  (if (bytes? image)
    (IOUtils/write image (output-stream output))
    (ImageIO/write image format output))
  output)

(defn get-rotations [metadata]
  (let [orientation (when metadata (get-in (exif/read metadata) ["Root" "Orientation"]))]
    (get orientation>rotations orientation)))

(defn without-exif [input & output-arg]
  (let [{:keys [bytes image metadata format]} (get-encoded-image input)
        rotations (get-rotations metadata)
        output (if (and image format rotations)
                  (reduce do-rotation image rotations)
                  bytes)]
      (if (instance? File (first output-arg))
        (to-file output format (first output-arg))
        (if (instance? File input)
          (to-file
             output
             format
             (File/createTempFile "clj-exif-orientation-" ".tmp"))
          (to-byte-array output format)))))

