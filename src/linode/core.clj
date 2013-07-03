(ns linode.core
  (:require [clj-http.client :as client]
            [clojure.string :as str]))

(defn- underscore-keys
  "Turn {:foo-bar _} into {:foo_bar _}.
   Prepares params for clj-http."
  [hmap]
  (let [underscore-key (fn [key]
                           (keyword (str/replace (name key) "-" "_")))]
    (into {} (map (fn [[k v]] [(underscore-key k) v]) hmap))))

(defn- hyphenize-keys
  "Turn {:FOO_BAR [{:BAZ_QUX _}]} into {:foo-bar [{:baz-qux _}]}.
   Used to Clojurize Linode's response body."
  [hmap]
  (let [hyphenize-key (fn [key]
                        (-> (str/replace (name key) "_" "-")
                            (str/lower-case)
                            (keyword)))]
    (into {} (map (fn [[k v]]
                    (cond
                      (map? v) [(hyphenize-key k) (hyphenize-keys v)]
                      (sequential? v) [(hyphenize-key k) (into [] (map hyphenize-keys v))]
                      :else [(hyphenize-key k) v]))
                  hmap))))

(defn request
  "Usage: (request :user.getapikey {:username _ :password _})
          (request :account.info {:api-key _})"
  [meth params]
  (let [url "https://api.linode.com"
        api-action (name meth)
        query-params (merge (underscore-params params)
                            {:api_action api-action})
        response (client/post url {:as :json
                                   :query-params query-params})]
    (hyphenize-keys (:body response))))
