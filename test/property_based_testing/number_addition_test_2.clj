(ns property_based_testing.number-addition-test-2
  (:use clojure.test)
  (:require [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :as ct :refer (defspec)]))

; Associativity.

(defspec number-addition-associativity-prop
  (prop/for-all [a gen/int b gen/int c gen/int]
    (= (+ a (+ b c)) (+ (+ a b) c))))

; Exercise: What happens if you use floats or bytes instead of ints?

; Commutativity.

(defspec number-addition-commutativity-prop
  (prop/for-all [a gen/int b gen/int]
    (= (+ a b) (+ b a))))

; Addition with the identity (zero)

(defspec number-addition-with-identity-prop
  (prop/for-all [a gen/int]
    (and (= a (+ a 0)) (= (+ a 0) a))))


