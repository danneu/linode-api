# linode-api

A simple Linode API wrapper for Clojure.

## Linode API

https://www.linode.com/api/

- Linode methods: https://www.linode.com/api/linode
- NodeBalancer methods: https://www.linode.com/api/nodebalancer
- StackScript methods: https://www.linode.com/api/stackscript
- DNS methods: https://www.linode.com/api/dns
- Utility methods: https://www.linode.com/api/utility

## Usage

``` clojure
(ns my.app
  (:use [linode-api.core]))
```

Every Linode API method is exposed as a function. Just replace "." with "-".

### Examples

Get your API key:

``` clojure
(user-getapikey {:username "danneu" :password "..."})
;=> {:errorarray [] 
;    :data {:username "danneu" 
;           :api-key "..."} 
;    :action "user.getapikey"}
```

All other methods require the `:api-key` param:

``` clojure
(account-info {:api-key api-key})
;=> {:errorarray []
;    :data {:transfer-used 1
;           :transfer-billable 0
;           :transfer-pool 2000
;           :active-since "2013-04-04 20:28:24.0"
;           :managed false}
;    :action "account.info"}
```

``` clojure
(avail-datacenters {:api-key api-key})
;=> {:errorarray [],
;    :data [{:location "Dallas, TX, USA" :datacenterid 2}
;           {:location "Fremont, CA, USA" :datacenterid 3}
;           {:location "Atlanta, GA, USA" :datacenterid 4}
;           {:location "Newark, NJ, USA" :datacenterid 6}
;           {:location "London, England, UK" :datacenterid 7}
;           {:location "Tokyo, JP" :datacenterid 8}]
;    :action "avail.datacenters"}
```

### Alternative API

Our you can use the `linode-request` directly. Pass in the API method-keyword and a map of params.

``` clojure
(linode-request :user.getapikey {:username "danneu" :password "..."})
(linode-request :account.info {:api-key api-key})
(linode-request :avail.datacenters {:api-key api-key})
```

## Todo

- Support error codes.
- Support batch-requests.
- Let user declare `:api-key` once to reduce REPL verbosity.
- Expose an alternative API: a method for each Linode call to reduce REPL verbosity.
- Add optional 3rd parameter: a map that will be merged into clj-http options.

## License

Distributed under the Eclipse Public License, the same as Clojure.
