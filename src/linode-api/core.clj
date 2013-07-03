(ns linode-api.core
  (:require [clj-http.client :as client]
            [clojure.string :as str]))

(defn- underscorize-keys
  "Turn {:foo-bar _} into {:foo_bar _}.
   Prepares params for clj-http."
  [hmap]
  (let [underscorize-key (fn [key]
                           (keyword (str/replace (name key) "-" "_")))]
    (into {} (map (fn [[k v]] [(underscorize-key k) v]) hmap))))

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

(defn linode-request
  "Usage: (linode-request :user.getapikey {:username _ :password _})
          (linode-request :account.info {:api-key _})"
  [meth params]
  (let [url "https://api.linode.com"
        api-action (name meth)
        query-params (merge (underscorize-keys params)
                            {:api_action api-action})
        response (client/post url {:as :json
                                   :query-params query-params})]
    (hyphenize-keys (:body response))))

;; Define all of our explicit API functions ;;;;;;;;;;;;;;;;;;;;;;;;;

(defmacro def-linode-method [meth-keyword]
  (let [meth-name (str/replace (name meth-keyword) "." "-")]
    `(defn ~(symbol meth-name) [params#]
       (linode-request ~meth-keyword params#))))

;; I couldn't figure out how to do this in a loop.
(def-linode-method :account.info)
(def-linode-method :api.spec)
(def-linode-method :avail.datacenters)
(def-linode-method :avail.distributions)
(def-linode-method :avail.kernels)
(def-linode-method :avail.linodeplans)
(def-linode-method :avail.stackscripts)
(def-linode-method :domain.create)
(def-linode-method :domain.delete)
(def-linode-method :domain.list)
(def-linode-method :domain.resource.create)
(def-linode-method :domain.resource.delete)
(def-linode-method :domain.resource.list)
(def-linode-method :domain.resource.update)
(def-linode-method :domain.update)
(def-linode-method :linode.boot)
(def-linode-method :linode.clone)
(def-linode-method :linode.config.create)
(def-linode-method :linode.config.delete)
(def-linode-method :linode.config.list)
(def-linode-method :linode.config.update)
(def-linode-method :linode.create)
(def-linode-method :linode.delete)
(def-linode-method :linode.disk.create)
(def-linode-method :linode.disk.createfromdistribution)
(def-linode-method :linode.disk.createfromstackscript)
(def-linode-method :linode.disk.delete)
(def-linode-method :linode.disk.duplicate)
(def-linode-method :linode.disk.list)
(def-linode-method :linode.disk.resize)
(def-linode-method :linode.disk.update)
(def-linode-method :linode.ip.addprivate)
(def-linode-method :linode.ip.list)
(def-linode-method :linode.job.list)
(def-linode-method :linode.list)
(def-linode-method :linode.reboot)
(def-linode-method :linode.resize)
(def-linode-method :linode.shutdown)
(def-linode-method :linode.update)
(def-linode-method :nodebalancer.config.create)
(def-linode-method :nodebalancer.config.delete)
(def-linode-method :nodebalancer.config.list)
(def-linode-method :nodebalancer.config.update)
(def-linode-method :nodebalancer.create)
(def-linode-method :nodebalancer.delete)
(def-linode-method :nodebalancer.list)
(def-linode-method :nodebalancer.node.create)
(def-linode-method :nodebalancer.node.delete)
(def-linode-method :nodebalancer.node.list)
(def-linode-method :nodebalancer.node.update)
(def-linode-method :nodebalancer.update)
(def-linode-method :stackscript.create)
(def-linode-method :stackscript.delete)
(def-linode-method :stackscript.list)
(def-linode-method :stackscript.update)
(def-linode-method :test.echo)
(def-linode-method :user.getapikey)
