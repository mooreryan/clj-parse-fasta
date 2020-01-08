# parse-fasta

A Clojure library that provides programmatic access to fasta files.

## Usage

Simple example.  The fasta file looks like this:

```text
>s1 apple
ACTG
n
>s2 pie
actg
n
```

And the parsing code:

```clojure
(ns example
  (:require [clojure.java.io :as io]))

(with-open [rdr (io/reader "./test_files/seqs.fa")]
  (let [recs (get-fasta-records rdr)]
    (doseq [rec recs]
      (println rec))))

;; Would print something like this....
; {:id "s1", :desc "apple", :seq "ACTGn"}
; {:id "s2", :desc "pie", :seq "actgn"}
```

It will also handle tricky fasta files like this one....

```
> empty seq at beginning
>seq1 is fun
AAC TGG NN N   


>seq2
     AAT     
CCTGNNN     
> empty seq 1
> empty seq 2
          

>seq3
yyyyyyyyyy 

     yyyyy    
NNN
>seq 4 > has many '>' in header
ACTG    
actg
>seq 5
          a      c     t     G      
>empty seq at end

```

Lots of weird stuff going on there.  Here is what parsing that would look like.

```clojure
(with-open [rdr (io/reader "./test_files/tricky.fa")]
  (into [] (get-fasta-records rdr)))

; Will return something like this....

;[{:id "" :desc "empty seq at beginning" :seq ""}
; {:id "seq1" :desc "is fun" :seq "AACTGGNNN"}
; {:id "seq2" :desc "" :seq "AATCCTGNNN"}
; {:id "" :desc "empty seq 1" :seq ""}
; {:id "" :desc "empty seq 2" :seq ""}
; {:id "seq3" :desc "" :seq "yyyyyyyyyyyyyyyNNN"}
; {:id "seq" :desc "4 > has many '>' in header" :seq "ACTGactg"}
; {:id "seq" :desc "5" :seq "actG"}
; {:id "empty" :desc "seq at end" :seq ""}]
```

*Note*:  `get-fasta-records` will throw if the input doesn't look like a
fasta file.

## License

Copyright © 2020 Ryan M. Moore

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
