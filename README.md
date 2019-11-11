# Text Search Application for Mimecast Test

## Requirement

This is a backend Java service that scans all file for its contents in a given folder and its
subfolders for a plain text search term, and returns the list of matching files, their number of
matches and the server they reside.
The frontend to accept the parameters and output the results can be written with your
preferred JS framework (Angular, React.js, Vue.js, etc). Do not use C# Asp.Net for the
frontend.
Service Requirements:
1. The service has to expose 2 endpoints
 One to get a list of servers
 One to perform the search on a given folder on the selected servers.
2. GetServers endpoint should return a list of available servers where to perform the
search in.
 This can be a hardcoded list of names for testing purposes.
 One of them should be “localhost”, so the search can be performed on the
same server executing the endpoint for testing purposes.

3. Search endpoint should accept the search parameters and return a list of results in
real time:
 Input parameters: The patch of the folder to scan.
 Input parameters: The search term to match.
 The result should be a list of matches returned as soon as they are found. Do
not wait to get all the results to send them back to the frontend. The search
should continue and all results should be returned eventually.

## How to build

    mvn clean install


## How to Run

    mvn spring-boot:run
    
## Swagger

This app was integrated with Swagger to document and test the endpoints. 
To access swagger's UI access the following link: 

	http://localhost:8080/swagger-ui.html#

Swagger UI is used as a front end to test the application and no separate front end application is built.

## API end-points

	http://localhost:8080/servers
	http://localhost:8080/search?servers=<server Name>&path=<Absolute Path>&searchString=<String to search>

Test Endpoint : http://localhost:8080/search?servers=localhost&path=c://projects//practice&searchString=swathi

Once application is Started, the console can be seen for : 

********************************************
The TextSearchApplication  application is started.
********************************************
Refer to the Swagger documentation at : http://localhost:8080/swagger-ui.html
******************************************** 
Enter Y/y to stop.

## Lombok for Eclipse 
This application is using Lombok library for the model classes. The same might show errors in eclipse. Make sure that lombok is properly configured in eclipse.
Refer https://www.journaldev.com/18124/java-project-lombok




