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

Think carefully about the use of a service layer in your application. You will need to use WebMVCTests to ensure your repositories work correctly.
