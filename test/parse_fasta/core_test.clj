(ns parse-fasta.core-test
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [parse-fasta.core :refer :all]
            [pjstadig.humane-test-output :as hto]))

(hto/activate!)

(def test-file-dir "./test_files/")
(def seqs-fa (str test-file-dir "seqs.fa"))
(def tricky-fa (str test-file-dir "tricky.fa"))

(deftest test--fasta-records
  (testing "It parses the simple fasta file."
    (let [lines (s/split-lines (slurp seqs-fa))
          actual (fasta-records lines)]
      (is (= [{:id "s1" :desc "apple" :seq "ACTGn"}
              {:id "s2" :desc "pie" :seq "actgn"}]
             actual))))
  (testing "It parses the tricky fasta file."
    (let [lines (s/split-lines (slurp tricky-fa))
          actual (fasta-records lines)]
      (is (= [{:id "" :desc "empty seq at beginning" :seq ""}
              {:id "seq1" :desc "is fun" :seq "AACTGGNNN"}
              {:id "seq2" :desc "" :seq "AATCCTGNNN"}
              {:id "" :desc "empty seq 1" :seq ""}
              {:id "" :desc "empty seq 2" :seq ""}
              {:id "seq3" :desc "" :seq "yyyyyyyyyyyyyyyNNN"}
              {:id "seq" :desc "4 > has many '>' in header" :seq "ACTGactg"}
              {:id "seq" :desc "5" :seq "actG"}
              {:id "empty" :desc "seq at end" :seq ""}]
             actual)))))



