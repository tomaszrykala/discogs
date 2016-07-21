# discogs

## building the project
This project fetches model from Discogs API, which for some of its calls that are used by the app, requires the API key and secret. A set of these needs to be present in the environment variables as:
- DiscogsConsumerKey
- DiscogsConsumerSecret

## obtaining and setting up the keys
- Information how to obtain the keys can be found at https://www.discogs.com/developers/#page:authentication
- The method how the keys were implemented on my machine at: http://www.rainbowbreeze.it/environmental-variables-api-key-and-secret-buildconfig-and-android-studio/