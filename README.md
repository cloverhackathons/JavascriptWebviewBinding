# Overview

Getting started with Android development can be challenging, especially if a majority of your coding experience is in web development, if you have limited exposure to Java, and if you're crunched for time at a hackathon :). Thankfully, there are ways to integrate your browser-based web application onto a Clover device itself in just a few easy steps.

## Potential use cases
- Have your web app start a Sale transaction on the Clover device
- Have your web app launch the Camera on the Clover device

## Code walkthrough

Both JavaScript and Android code is required to make this work. Let's talk about each one-by-one.

#### JavaScript code (react.jsx file)

React essentially combines the JavaScript code and HTML templating into one file (.jsx), whereas other frameworks might use a combination of JavaScript + Handlebars, for example. Some adaptation might be necessary to convert this code into something usable by your favorite frontend framework.

There are 3 primary HTML elements in this example code:

1. A form input that keeps track of the `amount`.
2. A button with an `onclick` handler. `onclick`, call the Clover `doSale` method provided by the Android code, pass in the `amount` as a parameter.
3. A `<div>` with `id="saleResponse"`. initially this element doesn't do anything. It just serves as a UI element that can be mutated when the `doSale` method from Android/Clover finishes.

Please keep in mind that .jsx syntax for React is different than other templating languages, or vanilla HTML. (For example, `class` => `className` in jsx, `onchange` => `onChange`, etc.)

#### Android code

