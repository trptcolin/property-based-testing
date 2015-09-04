(ns property_based_testing.zip-code-test-4
  (:use clojure.test)
  (:require [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :as ct :refer (defspec)]))

; Zip Codes don't support "addition", but we can constrain allowed values.
; Let's suppose we have a zip code validator that we want to test.
; For simplicity, we'll consider only 5-digit zip codes.

(defn valid-int [i] (and (>= i 0) (<= i 99999)))

(defn valid-zip-code? [x]
  (cond
    (integer? x) (valid-int x)
    (string?  x) (let [[i] [(Integer/parseInt x)]] (valid-int i))
    :else false))


; Test our validator:
; Note the new generator API calls we use.

(defspec valid-zip-code-accepts-valid-zip-code-numbers-prop
  (prop/for-all [i (gen/choose 0 99999)]
    (valid-zip-code? i)))

(defspec valid-zip-code-accepts-valid-zip-code-strings-prop
  (prop/for-all [i (gen/choose 0 99999)]
    (valid-zip-code? (format "%05d" i))))

; Test that invalid numbers are flagged. We want to use integers < 0 or > 99999.
(defspec valid-zip-code-rejects-invalid-zip-code-numbers-prop
  (prop/for-all [i (gen/such-that (fn [i] (not (valid-int i))) gen/int)]
    (not (valid-zip-code? i))))

(defspec valid-zip-code-rejects-invalid-zip-code-strings-prop
  (prop/for-all [i (gen/such-that (fn [i] (not (valid-int i))) gen/int)]
    (not (valid-zip-code? (format "%05d" i)))))

; Test invalid types
(defspec valid-zip-code-rejects-invalid-zip-code-floats-prop
  (prop/for-all [i gen/int]
    (not (valid-zip-code? (* 1.1 i)))))

; Exercise: Support the optional "-NNNN" suffix.
