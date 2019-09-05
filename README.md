# ws-clojure-sample

__Clojure websocket async app using Immutant__

## Config
__See__ `config/config.edn.example`

## Usage

```shell script
lein run
```

### REPL
```shell script
APP_CONFIG=config/config.edn lein repl

;; REPL starts in 'user namespace, type:
user=> (go)

;; Server will be started, REPL will be switched to 'dev namespace

dev=> (stop) ;; (reset), (start)

```


## License

Copyright © 2017 Lens

Distributed under the MIT license.
