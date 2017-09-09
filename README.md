because time is a circle, obvs.


## Development Mode

see application state:

```
re-frame.db/app-db
```


### Compile css:

Compile css file once.

```
lein sass once
```

Automatically recompile css file on change.

```
lein sass auto
```

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build

To compile clojurescript to javascript:

```
lein clean
lein cljsbuild once min
```

## TODO

- scrape some timezone data to parse time, ughh
- maybe use a dropdown component, gross

## Deploying to gh-pages

```
git subtree push --prefix resources/public origin gh-pages
```

(emergency escape hatch: `git push origin `git subtree split --prefix resources/public master`:gh-pages --force`)
