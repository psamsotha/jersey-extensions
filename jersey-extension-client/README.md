Jersey Extension Feature Client
---

This project is a client of the feature developed in the sibling project
`jersey-extension-framework`. Explanation of that project can be found in the blog
[Creating Reusable Jersey Extensions][1]

[1]: 

To run the this project, you can use the `jetty-maven-plugin` included in the pom.xml
or you can run it on your favorite Servlet container.

I will explain the steps using the `jetty-maven-plugin`

1. `cd` to the parent project.
2. Run `mvn clean install`
3. `cd` back to this project.
4. Run `mvn jetty:run`

To test I will show cURL commands, but you can use your favorite REST client. The
steps will be as follows.

1. Make a GET request to dummy endpoint `http://localhost:8080/api/dummy` to 
see that we are unauthorized. We will get back a `401 Unauthorized`.
2. Make a POST request with `application/x-www-form-urlencoded` data consisting
of the `clientId` and `clientPass`. We get back an API key.
3. Make a GET request to the dummy endpoint again, but this time with the key

Step 1

    C:\>curl -i http://localhost:8080/api/dummy
    HTTP/1.1 401 Unauthorized
    Date: Fri, 23 Oct 2015 07:58:03 GMT
    Content-Length: 0
    Server: Jetty(9.2.6.v20141205)

Step 2

    C:\>curl -i -X POST http://localhost:8080/api/apikey \
             -d "clientId=725e32d9-4bc4-4a81-aac4-6fa6b93ba2a6" \
             -d "clientPass=secret"
    HTTP/1.1 200 OK
    Date: Fri, 23 Oct 2015 07:59:06 GMT
    Content-Type: application/json
    Content-Length: 49
    Server: Jetty(9.2.6.v20141205)

    {"apiKey":"153fc95e-0805-4862-891b-5f58fa2dabbf"}

The `clientId` and `clientSecret` is from data we initially added to the data store. 
See the `import.sql` file.

Step 3.

    C:\>curl -i http://localhost:8080/api/dummy 
             -H "X-Api-Token: 153fc95e-0805-4862-891b-5f58fa2dabbf"
    HTTP/1.1 200 OK
    Date: Fri, 23 Oct 2015 08:01:53 GMT
    Content-Type: text/plain
    Content-Length: 10
    Server: Jetty(9.2.6.v20141205)

    Dummy Data

The `Dummy Data` is the response from our `DummyResource`.
