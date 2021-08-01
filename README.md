![feature-graphic-wide Kopie](https://user-images.githubusercontent.com/46780390/127783140-655ab2ab-2f4f-4346-a2f1-4c07f621890d.PNG)

# Bitpot Overview

Bitpot is an Android app that enables you to access your Bitbucket repositories from your mobile phone.  
A short list of the most important features includes:

For each repository you can:
 * Browse its source-code by branch and tag
 * View and share any text file (with syntax highlighting for most languages)
 * List all current Pipelines, Pull Requests and Downloads
 * Get Push Notifications for changes in Pull Requests or Pipelines

Features on Pull Requests include:
 * See the PR description, all participants, comments, changed files, etc.
 * You can approve and un-approve each PR
 * You can reply to existing comments
 * You can create new comments for each line, file or the whole PR
 * You can delete your own comments
 * You can merge the PR

# Get The App

The easiest way to install Bitpot is by using the Google PlayStore:

[<img src="https://lh3.googleusercontent.com/cjsqrWQKJQp9RFO7-hJ9AfpKzbUb_Y84vXfjlP0iRHBvladwAfXih984olktDhPnFqyZ0nu9A5jvFwOEQPXzv7hr3ce3QVsLN8kQ2Ao=s0">](https://play.google.com/store/apps/details?id=net.onefivefour.android.bitpot)

# Build The App

Of course, you can also clone this repository and build the APK using [Android Studio](https://developer.android.com/studio)

To do so, you need to clone this repository and additionally create the following files that are not checked into Git:

`signing.properties`:

```
signing.keystorePath=Z:/Path/To/The/Keystore/File/keystore.jks
signing.keystorePassword=TheKeystorePassword
signing.keyAlias=AnAliasForTheKey
signing.keyPassword=TheKeyPassword
```

`bitbucket.properties`:

```
bitbucket.clientSecret="YourBitbucketClientSecret"
bitbucket.clientId="YourBitbucketClientId"
```

See the [Bitbucket documentation on how to create a Client Secret](https://www.jetbrains.com/help/hub/bitbucket-cloud-auth-module.html#generate-client-id-secret) for more details.

`local.properties`: Let this file be created by AndroidStudio. It contains your local path to the Android SDK.

`google-services.json`: You can get a google-services.json from Firebase. Create a new Firebase project and download the file as described [here](https://support.google.com/firebase/answer/7015592)

Put all 3 `.properties` files into the root directory.
Put the `google-services.json` into the `/app` directory.

Now you should be able to build and run Bitpot using this source code.


# Contributing
Pull requests are welcome. For major changes, please open an [issue](https://github.com/OneFiveFour/Bitpot/issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

# License
[GPL-3.0 License](https://github.com/OneFiveFour/bitpot/blob/main/LICENSE)
