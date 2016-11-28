package com.mehcode.reactnative.braintree;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeListener;
import com.braintreepayments.api.interfaces.BraintreeResponseListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableNativeMap;

class BraintreeModule extends ReactContextBaseJavaModule implements PaymentMethodNonceCreatedListener {
  private BraintreeFragment mBraintreeFragment;
  private Promise mPromise;

  public BraintreeModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
      return "RNBraintree";
  }

  @ReactMethod
  public void configure(String key, Promise promise) {
    try {
      mBraintreeFragment = BraintreeFragment.newInstance(getCurrentActivity(), key);
      mBraintreeFragment.addListener(this);
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
    }

    promise.resolve(null);
  }

  @Override
  public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
    WritableNativeMap options = new WritableNativeMap();
    options.putString("nonce", paymentMethodNonce.getNonce());
    options.putString("description", paymentMethodNonce.getDescription());

    mPromise.resolve(options);
  }

  @ReactMethod
  public void tokenize(ReadableMap options, Promise promise) {
    mPromise = promise;

    ReadableMap card = options.getMap("card");
    CardBuilder cardBuilder = new CardBuilder()
            .cardholderName(card.getString("name"))
            .expirationYear(card.getString("expirationYear"))
            .expirationMonth(card.getString("expirationMonth"))
            .cvv(card.getString("cvv"))
            .cardNumber(card.getString("number"));

    Card.tokenize(mBraintreeFragment, cardBuilder);
  }
}
