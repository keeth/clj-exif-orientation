(defproject clj-exif-orientation "0.2.0"
  :description "Auto-rotate JPEG images using EXIF orientation metadata"
  :url "http://github.com/keeth/clj-exif-orientation"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.imgscalr/imgscalr-lib "4.2"]
                 [clj-exif "0.1"]
                 [commons-io/commons-io "2.4"]]
  :java-source-paths ["java-src"]
  :profiles {
    :dev {
      :dependencies []}})
