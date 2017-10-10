# Overview

Getting started with Android development can be challenging, especially if a majority of your coding experience is in web development, if you have limited exposure to Java, or if you're crunched for time at a hackathon. Thankfully, there are ways to integrate your browser-based web application onto a Clover device itself in just a few easy steps.

## Potential use cases
- Have your web app start a Sale transaction on the Clover device
- Have your web app launch the Camera on the Clover device

**Pre-requisites**

Your web application's page must:
- Be deployed (https://devcenter.heroku.com/start)
- Be HTTPS-enabled (https://blog.heroku.com/ssl-is-now-included-on-all-paid-dynos)
- Not use Android WebView-incompatible JavaScript (e.g., `window.location.href`)

## Video demo

https://vimeo.com/237588664

The demo itself is quick, and then goes into additional detail explaining the underlying code, for those who are interested. This README also explains the underlying code.

## Code walkthrough

Both JavaScript and Android code is required to make this work. Let's talk about each one-by-one.

### JavaScript code (react.jsx file)

React essentially combines the JavaScript code and HTML templating into one file (.jsx), whereas other frameworks might use a combination of JavaScript + Handlebars, for example. As a result, some adaptation might be necessary to convert this code into something usable by your favorite frontend framework.

There are 3 HTML elements in this example code that are points of interest:

1. A form input that keeps track of the `amount`.
2. A button with an `onclick` handler. `onclick`, call the Clover `doSale` method provided by the Android code, pass in the `amount` as a parameter.
3. A `<div>` with `id="saleResponse"`. initially this element doesn't do anything. It just serves as a UI element that can be mutated when the `doSale` method from Android/Clover finishes.

Please keep in mind that .jsx syntax for React is different than other templating languages, or vanilla HTML. (For example, `class` => `className` in jsx, `onchange` => `onChange`, etc.)

### Android code

#### activity_webview_android_binding.xml

A simple layout file with an [Android WebView](https://developer.android.com/reference/android/webkit/WebView.html). Most Clover devices do not have a browser due to PCI compliance, but webpages can still be rendered in WebViews.

#### WebviewAndroidBinding.java

TODO: Change the private `PAGE_URL` variable to your web application's URl.

In `onCreate`, we will load this `PAGE_URL` and provide methods to the JavaScript context from within the `WebAppInterface` class.

The `doSale` method uses code provided by the [clover-android-sdk](https://github.com/clover/clover-android-sdk). Specifically, it fires [an Intent](https://github.com/clover/clover-android-sdk/blob/ba1bc1e5ab0c2189108b3c4b6de6c79c4fb6065b/clover-android-sdk/src/main/java/com/clover/sdk/v1/Intents.java) to launch Clover's Payments application. 

When the Payments application completes, `onActivityResult()` is invoked as a callback. In `onActivityResult()`, we use `WebView#loadUrl()` to inject plain old JavaScript into the web application. In this case, we invoke the `window.onSaleResponse()` method that's provided in the `react.jsx` file.