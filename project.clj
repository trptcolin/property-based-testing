(defproject property-based-testing "0.1.0-SNAPSHOT"
  :description "Simple exercises in property based testing using Clojure's test.check"
  :url "https://github.com/deanwampler/property-based-testing"
  :license {:name "Apache License - v 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/test.check "0.8.1"]]
  :main ^:skip-aot property-based-testing.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
