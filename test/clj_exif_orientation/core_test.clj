(ns clj-exif-orientation.core-test
  (:import (org.apache.commons.io IOUtils)
           (java.util Arrays))
  (:require [clojure.test :refer :all]
            [clj-exif-orientation.core :refer :all]
            [clojure.java.io :as io]))

(defn write-to-stream [bytes out]
  (IOUtils/write bytes out))

(deftest a-test
  (testing "transform orientation 6 to orientation 1"
      (let [input (IOUtils/toByteArray
                   (io/input-stream "test-resources/chips-orientation-6.jpg"))
            expected (IOUtils/toByteArray
                       (io/input-stream "test-resources/chips-orientation-1.jpg"))
            output (transform-byte-array input)]
        (is (Arrays/equals expected output)))))
