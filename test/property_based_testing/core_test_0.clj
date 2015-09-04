; Use clojure.test for traditional-style tests.
; Note that the package property-based-testing.core is just this projects
; source code, which is a trivial main routine.

(ns property-based-testing.core-test-0
  (:require [clojure.test :refer :all]
            [property-based-testing.core :refer :all]))

(deftest a-dummy-passing-test
  (testing "A trivial, regular-style test."
    (is (= 0 0))))
