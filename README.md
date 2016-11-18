# React Native Braintree
> React Native interface to Braintree's Android and iOS SDKs

## Install

1. `npm install --save rn-braintree`

2. `react-native link rn-braintree`

## Usage

### Configure

```js
import Braintree from "rn-braintree";

Braintree.configure("<token>");
```

### Tokenize

```js
import Braintree from "rn-braintree";

// Braintree.configure must have been called previously
Braintree.tokenize({
  card: {
    number: "4111111111111111",
    expirationMonth: "12",
    expirationYear: "2020",
    cvv: "123",
  },
}).then((result) => {
  // result.nonce => "..."
  // result.type => "visa"
  // result.isDefault => true
  // result.description => "..."
}).catch((err) => {
  // [...]
});
```
