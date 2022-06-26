# StatementApp - Instructions

In order to execute the project checkout the repository and change the directory into the project folder and execute the following commands:

  1- mvn clean install
  
  2- Change the directory to target folder
  
  3- Then to run the jar execute command: java -jar jar-name.
  

#### In order to run it from some ide, just checkout the code and use java 1.8 version to execute.
1- Database file path can be modified in the application.properties file.

2- Session handling has been implemented using custom authorization server class and session token is generated using spring OAuth2.

3- Session validity is 5 minutes.

4- In case of error the relevant error message is returned.

5- There are only two users hardcoded as of now, which can be used to test the app (Given below). 



Following requests can be used in sequential order to test the flow of the Statement App:

#### The following are the users that can be used to test the app:

Admin => username: admin  |   password: admin

User => username: user    |   password: user

#### First step is to get the access token, execute the following request with the valid user:


```
curl --location --request POST 'http://localhost:9090/oauth/token' \
--header 'Authorization: Basic d2ViOndlYg==' \
--form 'grant_type="password"' \
--form 'username="user"' \
--form 'password="user"'
```


#### Second step is to get the resource by hitting statements API (Include the bearer token received in the first request reponse in the below request):

```
curl --location --request GET 'http://localhost:9090/account/statements' \
--header 'Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoibmFnYXJybzEwMDIiLCJ1c2VyX25hbWUiOiJ1c2VyIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXSwiZXhwIjoxNjU2MjY1MTgxLCJhdXRob3JpdGllcyI6WyJVU0VSIl0sImp0aSI6IjNlZDJmNjA2LTA5ZWYtNDI5Mi1hOGM5LTQxYzFjMTY4N2E1YiIsImNsaWVudF9pZCI6IndlYiJ9.-TXkcXbbbdnqAPPZaoWjx60LJyTSEi2LjyZuaz1GgOw' \
--header 'Content-Type: application/json' \
--data-raw '{
    "accountId" : 2,
    "fromDate" : "25-6-2020",
    "toDate" : "26-6-2022",
    "fromAmount" : 10.0,
    "toAmount" : 400
}'
```


```curl --location --request GET 'http://localhost:9090/account/statements' \
--header 'Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoibmFnYXJybzEwMDIiLCJ1c2VyX25hbWUiOiJ1c2VyIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIiwidHJ1c3QiXSwiZXhwIjoxNjU2MjY1MTgxLCJhdXRob3JpdGllcyI6WyJVU0VSIl0sImp0aSI6IjNlZDJmNjA2LTA5ZWYtNDI5Mi1hOGM5LTQxYzFjMTY4N2E1YiIsImNsaWVudF9pZCI6IndlYiJ9.-TXkcXbbbdnqAPPZaoWjx60LJyTSEi2LjyZuaz1GgOw' \
--header 'Content-Type: application/json' \
--data-raw '{}'```



