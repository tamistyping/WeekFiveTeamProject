# Spring Project: Interacting with MySQL World Database

## Requirements (Part 1: JPA)

You have been asked to create a Spring program to interact with the MySQL World Database. This Database is made up of 3 tables which represent:

- **country**: Information about countries of the world.
- **city**: Information about some of the cities in those countries.
- **countrylanguage**: Languages spoken in each country.

You will need to use Spring JPA to connect and communicate with the Database. Along with basic CRUD operations, you will need to find out the following:

1. Which countries have no Head of State?
2. What percentage of a given country's population lives in its largest city?
3. Which country has the most cities? How many cities does it have?
4. Which 5 districts have the smallest population?
5. For a given country, approximately how many people speak its most popular official language?

This project in its current state is the backend for a website to query the MySQL world database, with no current application. Methods for querying the database have been implemented utilising a service layer. These methods have been thorougly tested through the use of JUnit unit testing and the following features are finished:
- Find country by country code
- Create a new country, city, or country language entry
- Update all current country's fields, excluding country code
- Update all current city's fields
- update all current country language fields
- Delete a Country, city or country language from the database
- Find all countries
- Find all cities
- Find all Country Languages
- Find the percentage of population that lives in a given country's largest city
- Find which country has the most cities, and how many cities the country has
- Find which countries have no head of state
- Find which 5 districts have the smallest population
- Find the most popular offical language in a given country

## Requirements (part 2: Spring REST API
Following on from the last project, you have been asked to creat a RESTful API that exposes data from the MySQL World Database

- The API should allow someone to perform CRUD operation on the 3 tables
- You should use good error handling to report on misuse of the API with good messaging and correct status codes
- Think carefully about the design of your API and how you will use path variables, request bodies ond request parameters

In addition, you will need to research how to do the following:
- Secure the endpoints using an API key. Someone without the key with only be able to read data (Not using Spring Security)
- Use Swagger UI to provide a graphical interface to your API

## API functionality
The following commands are operations you can perform with our API

- /api/countries (with GET request gets all countries from world database)
- /api/countries/{id} (with GET request and id being a 3 character country code, retrieves specific country)
- /api/countries/languages/{language} (with GET request and id being a language, retrieves all countries that speak that language)
- /api/countries/with-no-head-of-state (with GET request retrieves all countries that have no head of state)
- /api/countries/with-most-cities (with GET request retrieves the country with the most cities (currently China))
- /api/countries/secure (with POST request and countryEntity body creates a new country)
- /api/countries/secure/{id} (with PUT request and countryEntity body updates an existing country by given three character country code, with DELETE request deletes given country by given country code)
- /api/cities (with GET request gets all cities from world database)
- /api/cities/{id} (with GET request gets city matching given id, city id is between 1 and 4 digits)
- /api/cities/secure (with POST request and CityEntity body creates a new city)
- /api/cities/secure/{id} (with PUT request and cityEntity body updates an existing country by given 1-4 digit id, with DELETE request deletes given city by given id)
- /api/languages (with GET request retrieves all languages spoken by each country)
- /api/languages/all-languages (with GET request lists all unique languages in the world database)
- /api/languages/{id} (with GET request and id being a 3 character country code, retrieves specific country and its languages)
- /api/languages/{id}/{language} (with GET request and id being a 3 character country code, and language being a language, retrieves the language)
- /api/languages/secure (with POST request and countryLanguageEntity body creates a new language)
- /api/languages/secure/{id}/{language} (with PUT request and id being a 3 character country code, and language being a language, updates the language, with DELETE request deletes the language)

# example bodies

- new/update country
  {
  "code":"GMC",
  "name":"Grand Monarchy of Chris",
  "continent":"Europe",
  "region":"Southern Europe",
  "surfaceArea": 468.00,
  "indepYear": 1995,
  "population": 2000000,
  "lifeExpectancy": 90.5,
  "gnp": 2000,
  "localName": "Best Country Ever",
  "governmentForm": "Absolute Monarchy",
  "headOfState": "Chris",
  "capital": null,
  "code2": "MC",
  "gnpold": null
}

- new/update city
  {
  "name":"Monarchia",
  "countryCode":{
    "code": "GMC",
    "name": "Grand Monarchy of Chris",
    "continent": "Europe",
    "region": "Southern Europe",
    "surfaceArea": 468.00,
    "indepYear": 1995,
    "population": 2000000,
    "lifeExpectancy": 90.5,
    "gnp": 2000.00,
    "localName": "Best Country Ever",
    "governmentForm": "Absolute Monarchy",
    "headOfState": "Chris",
    "capital": null,
    "code2": "MC",
    "gnpold": null
  },
  "district":"Monarchia",
  "population":2000000
}

- new/update language
{
  "id": {
    "countryCode":"GMC",
    "language": "English"
  },
  "countryCode":{
    "code": "GMC",
    "name": "Grand Monarchy of Chris",
    "continent": "Europe",
    "region": "Southern Europe",
    "surfaceArea": 468.00,
    "indepYear": 1995,
    "population": 2000000,
    "lifeExpectancy": 90.5,
    "gnp": 2000.00,
    "localName": "Best Country Ever",
    "governmentForm": "Absolute Monarchy",
    "headOfState": "Chris",
    "capital": null,
    "code2": "MC",
    "gnpold": null
  },
  "isOfficial": "T",
  "percentage": 97.3
}

# API Key
in order to create, update or delete any entity from the database, key validation is required.


