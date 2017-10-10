public class WebviewAndroidBinding extends AppCompatActivity {
  // https://developer.android.com/guide/webapps/webview.html#BindingJavaScript

  private final int WEB_POS_SALE_REQUEST_CODE = 111;
  private final String TAG = "WebviewAndroidBinding";
  private WebView webView;
  private final String PAGE_URL = "https://creditstaff.io/android-binding";
  // TODO: replace with the webpage you want to load. Must be HTTPS.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_webview_android_binding);

    webView = (WebView) findViewById(R.id.WebApp);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.addJavascriptInterface(new WebAppInterface(this), "Android");

    webView.setWebViewClient(new WebViewClient());

    webView.loadUrl(PAGE_URL);
  }

  private class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
      mContext = c;
    }

    /** Fire an intent to launch Clover Secure Pay from the web page */
    @JavascriptInterface
    public String doSale(String amount) {
      // data comes in as a String, so pass in something like a JSON string when more complex data is required
      Intent i = new Intent(Intents.ACTION_SECURE_PAY);
      i.putExtra(Intents.EXTRA_AMOUNT, Long.valueOf(amount));
      startActivityForResult(i, WEB_POS_SALE_REQUEST_CODE);
      return "result string data";
      // this will get immediately returned to the JavaScript context
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case (WEB_POS_SALE_REQUEST_CODE):
        if (resultCode == RESULT_OK) {
          // good
          Payment p = data.getParcelableExtra(Intents.EXTRA_PAYMENT);

          // JavaScript didn't respond very well to brackets... so just creating data without them
          // logcat would log stuff like, "E/Web Console, unexpected '{' ...."
          String resultString = new StringBuilder()
                  .append("PaymentUUID: ")
                  .append(p.getId())
                  .append(", Amount: ")
                  .append(p.getAmount())
                  .append(", last4: ")
                  .append(p.getCardTransaction().getLast4())
                  .append(", cardholderName: ")
                  .append(p.getCardTransaction().getCardholderName()).toString();

          webView.loadUrl("javascript:window.onSaleResponse('" + resultString + "');");

        } else if (resultCode == RESULT_CANCELED) {
          // not happy path
        }
    }
  }
}
