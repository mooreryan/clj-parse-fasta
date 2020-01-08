(defproject parse-fasta "0.0.1"
  :description "Provides programmatic access to fasta files."
  :url "https://github.com/mooreryan/clj-parse-fasta"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [criterium "0.4.5"]]
  :profiles {:dev {:dependencies [[pjstadig/humane-test-output "0.10.0"]]
                   :plugins [[com.jakemccrary/lein-test-refresh "0.24.1"]]}}
  :repl-options {:init-ns parse-fasta.core})
