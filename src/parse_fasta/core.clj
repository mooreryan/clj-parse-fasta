(ns parse-fasta.core
  [:require [clojure.java.io :as io]
            [clojure.string :as s]
            [criterium.core :as bench]
            [clojure.core.reducers :as r]])

(defn- parse-header [line]
  (let [[id & desc] (-> line s/trim
                        (s/replace-first ">" "")
                        (s/split #" "))]
    {:id id :desc (if desc (s/join " " desc) "") :seq []}))

(defn- header?
  [line]
  (s/starts-with? line ">"))

(def not-header? (complement header?))

(defn- finish-record
  [rec]
  (update rec :seq s/join))

(defn- first-header?
  [line current-rec]
  (and (nil? current-rec) (header? line)))

(defn- clean-sequence [line]
  (-> line
      s/trim
      (s/replace #" " "")))

(defn- update-rec
  [line current-rec]
  (if (header? line)
    (parse-header line)
    (update current-rec :seq conj (clean-sequence line))))

(defn check-fasta-format
  [lines]
  (if (= \> (first (first lines)))
    lines
    (throw (Exception. "File doesn't look like a fasta."))))

(defn fasta-records
  "Don't forget to check the fasta format before calling this!"
  ([lines]
   (fasta-records lines [] nil))
  ([lines records rec]
   (if (seq lines)
     (let [recur-fn (partial fasta-records (rest lines) records)
           line (first lines)
           updated-rec (update-rec line rec)]
       (if (or (first-header? line rec)
               (not-header? line))
         (lazy-seq (recur-fn updated-rec))
         (cons (finish-record rec)
               (lazy-seq (recur-fn updated-rec)))))
     (conj records (finish-record rec)))))

(defn get-fasta-records
  "`rdr` is a reader, e.g., something returned by io/reader.

  Will throw if input doesn't look like a fasta  file."
  [rdr]
  (-> rdr line-seq check-fasta-format fasta-records))
