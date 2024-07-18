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
