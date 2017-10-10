const React = require('react');

const AndroidBinding = React.createClass({
  getInitialState() {
    return ({
      amount: 0
    });
  },

  render() {
    return (
      <div className="container">
        <div className="row">
          <div className="col-xs-12">
            <div>
              <input onChange={this.onAmountChange} />
            </div>

            <button onClick={this.doSale} >
              Sale
            </button>

            <div id="saleResponse">
              Response: {this.state.saleResponse}
            </div>
          </div>
        </div>
      </div>
    );
  },

  doSale(e) {
    e.preventDefault();
    
    // Clover.doSale() invokes the doSale method provided by our Android code
    // JavaScript can transfer data to Android by invoking Android methods,
    // and passing in data as parameters.
    const result = Clover.doSale(this.state.amount);
    // Android can transfer data to JavaScript through method return values.
    // In this case, the Clover#doSale method from Android returns data that we can capture in JavaScript.
    
    // these console.logs can be viewed from the Android device logs
    // 'adb logcat'
    // Download Android Studio, or just the command line tools, from https://developer.android.com/studio/index.html
    console.log("next line is a return value from Clover's context:");
    console.log(result);
  },

  onAmountChange(e) {
    this.setState({
      amount: e.target.value
    });
  }
});

// Android can also transfer data to JavaScript through callbacks.

// Exposing a callback in an API that would be global to Android, and not
// context (i.e., React component) specific. For this example, the 'window'
// object has global scope, so it can be accessed by our Android code.
window.onSaleResponse = function(response) {
  // in React, this.setState() is the best way to update the UI, but this does
  // not work here because we are bound to the context of the 'window', not the React
  // component itself. As such, we need to use context-agnostic code if we want to update
  // the UI.
  
  // the DOM API is one way we can update the UI, so let's do that.
  document.getElementById("saleResponse").innerHTML = "Response: " + response;

  // i.e., we can't this.setState({
  //   saleResponse: response
  // })
},

module.exports = AndroidBinding;
