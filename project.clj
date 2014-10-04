(defproject clj-exif-orientation "0.1.0"
  :description "Transform EXIF files to have regular top-left orientation"
  :url "http://github.com/keeth/clj-exif-orientation"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.imgscalr/imgscalr-lib "4.2"]
                 [clj-exif "0.1"]]
  :java-source-paths ["java-src"]
  :profiles {
    :dev {
      :dependencies [[commons-io/commons-io "2.4"]]}})
