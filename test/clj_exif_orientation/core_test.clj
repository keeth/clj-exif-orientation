(ns clj-exif-orientation.core-test
  (:import (org.apache.commons.io IOUtils)
           (java.util Arrays)
           (java.io File))
  (:require [clojure.test :refer :all]
            [clj-exif-orientation.core :refer :all]
            [clojure.java.io :as io]))

(defn write-to-stream [bytes out]
  (IOUtils/write bytes out))

(deftest basics
  (testing "reads a byte array, returns a byte array"
      (let [input (IOUtils/toByteArray
                   (io/input-stream "test-resources/input-6.jpg"))
            expected (IOUtils/toByteArray
                       (io/input-stream "test-resources/expected-6.jpg"))
            output (without-exif input)]
        (is (Arrays/equals expected output))))

  (testing "reads an input file, returns a tempfile"
    (let [expected (IOUtils/toByteArray
                     (io/input-stream "test-resources/expected-6.jpg"))
          output-file (without-exif (io/file "test-resources/input-6.jpg"))
          output (IOUtils/toByteArray (io/input-stream output-file))]
      (is (Arrays/equals expected output))))

  (testing "reads an input file, writes output to file given in second arg, returns output file"
    (let [expected (IOUtils/toByteArray
                     (io/input-stream "test-resources/expected-6.jpg"))
          output-arg (File/createTempFile "exif-test-" ".tmp")
          return-value (without-exif
                        (io/file "test-resources/input-6.jpg")
                        output-arg)
          output (IOUtils/toByteArray (io/input-stream output-arg))]
      (is (= output-arg return-value))
      (is (Arrays/equals expected output)))))

(deftest exceptions
  (testing "passes through file without exif"
    (let [input (IOUtils/toByteArray
                  (io/input-stream "test-resources/no-exif.jpg"))
          output (without-exif input)]
      (is (Arrays/equals input output))))

  (testing "passes through unparseable data"
    (let [input (byte-array [1 2 3])
          output (without-exif input)]
      (is (Arrays/equals input output))))

  (testing "passes through image where no rotation is required"
    (let [input (IOUtils/toByteArray
                  (io/input-stream "test-resources/input-1.jpg"))
          output (without-exif input)]
      (is (Arrays/equals input output)))))