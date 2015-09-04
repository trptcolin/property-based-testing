(ns property_based_testing.string-addition-test-3
  (:use clojure.test)
  (:require [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :as ct :refer (defspec)]))

; Associativity.

; (defspec string-addition-associativity-prop
;   ?
;   )

; Commutativity.

; (defspec string-addition-commutativity-prop
;   ?
;   )

; Addition with the identity. What is the identity?

; (defspec string-addition-with-identity-prop
;   ?
;   )

