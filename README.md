<h1>Clear Solution Test Task</h1>

<h2>Project Description</h2>

<h3>Table of Contents</h3>

  <ul>
        <li><a href="#requirements">Requirements</a></li>
        <li><a href="#getting-started">Getting Started</a></li>
        <li><a href="#endpoints">Endpoints</a></li>
        <li><a href="#technologies-used">Technologies Used</a></li>
  </ul>

<h3 id="requirements">Requirements</h3>

  <p>This project is designed to meet the following requirements:</p>

  <ol>
        <li>
            <p><strong>Fields:</strong></p>
            <ul>
                <li><strong>Email (required):</strong> Add validation against email pattern.</li>
                <li><strong>First name (required)</strong></li>
                <li><strong>Last name (required)</strong></li>
                <li><strong>Birth date (required):</strong> Must be earlier than the current date.</li>
                <li><strong>Address (optional)</strong></li>
                <li><strong>Phone number (optional)</strong></li>
            </ul>
        </li>
        <li>
            <p><strong>Functionality:</strong></p>
            <ul>
                <li><strong>Create User:</strong> Allows registration of users who are more than 18 years old. The
                    minimum age requirement should be configurable via a properties file.</li>
                <li><strong>Update User Fields:</strong> Update one or more fields of a user.</li>
                <li><strong>Update All User Fields:</strong> Update all fields of a user.</li>
                <li><strong>Delete User:</strong> Remove a user from the system.</li>
                <li><strong>Search for Users by Birth Date Range:</strong> Retrieve a list of users within a specified
                    birth date range. Validate that the "From" date is less than the "To" date.</li>
            </ul>
        </li>
        <li>
            <p><strong>Testing:</strong></p>
            <ul>
                <li>Code should be thoroughly covered by unit tests using Spring.</li>
            </ul>
        </li>
        <li>
            <p><strong>Error Handling:</strong></p>
            <ul>
                <li>Implement error handling for RESTful API endpoints to provide meaningful error messages to
                    clients.</li>
            </ul>
        </li>
        <li>
            <p><strong>API Responses:</strong></p>
            <ul>
                <li>API responses should be in JSON format.</li>
            </ul>
        </li>
  </ol>

<h3 id="getting-started">Getting Started</h3>

  <p>These instructions will guide you through setting up and running the project on your local development environment.</p>

<h4>Prerequisites</h4>

  <p>Before you begin, ensure you have met the following requirements:</p>

  <ul>
        <li>Java 17: Make sure you have Java 17 installed on your machine. You can download it from the <a
                href="https://www.oracle.com/java/technologies/javase-downloads.html">official Oracle website</a> or use a
            distribution like OpenJDK.</li>
  </ul>

<h4>Clone the Repository</h4>

  <ol>
      <li>Clone this repository to your local machine using your preferred method (HTTPS or SSH):</li>
      <pre><code>git clone https://github.com/your-username/your-project.git</code></pre>  
  </ol>

  <p>Replace <code>your-username</code> and <code>your-project</code> with the appropriate GitHub username and project
        repository name.</p>

<h4>Build and Run the Project</h4>

  <ol>
        <li>Navigate to the project directory:</li>
        <pre><code>cd your-project</code></pre>
  </ol>

  <ol start="2">
        <li>Run docker-compose.yml file</li>
  </ol>

  <ol start="3">
        <li>Create database,sequences and tables using sql scripts form database folder</li>
  </ol>

  <ol start="4">
        <li><strong>Optional:</strong> Configure database url in application.yml</li>
  </ol>

  <ol start="5">
        <li>Build the project using Gradle (or the build tool of your choice):</li>
        <pre><code>./gradlew build</code></pre>  
        <p>This command will download project dependencies and compile the code.</p>
</ol>

  <ol start="6">
        <li>Start the Spring Boot application:</li>
        <pre><code>./gradlew bootRun</code></pre>
        <p>or</p>
        <pre><code>docker-compose up</code></pre>
        <p>But if you use docker, then first you need to create an image of the project</p>
        <pre><code>./gradlew bootBuildImage</code></pre>
        <p>One of this commands will start the application, and it should now be accessible at <a href="http://localhost:8080"
            target="_blank">http://localhost:8080</a> in your web browser.</p>
  </ol>

<h4>Using the API</h4>

  <p>You can now use the RESTful API endpoints described in the <a href="#api-endpoints">API Endpoints</a> section to
        interact with the application.</p>

<h4>Running Unit Tests</h4>

  <p>To run the unit tests for the project, you can use the following command:</p>

  <pre><code>./gradlew test</code></pre>


<h3 id="endpoints">Endpoints</h3>

  <p>List and describe the available RESTful API endpoints of the application. Include details such as HTTP methods,
        request parameters, request bodies, and expected responses.</p>
  <h4>Create User</h4>
<ul>
    <li><strong>HTTP Method:</strong> POST</li>
    <li><strong>Endpoint:</strong> /api/users</li>
    <li><strong>Consumes:</strong> JSON (application/json)</li>
    <li><strong>Produces:</strong> JSON (application/json)</li>
    <li><strong>Request Body:</strong> A JSON object representing user details (UserCreateRequest)</li>
    <li><strong>Validation:</strong> Validates the request body against the UserCreateRequest class using @Valid.</li>
    <li><strong>Response:</strong> Returns a UserResponse representing the created user.</li>
    <li><strong>HTTP Status:</strong> 201 Created</li>
</ul>

<h4>Get User by Id</h4>
<ul>
    <li><strong>HTTP Method:</strong> GET</li>
    <li><strong>Endpoint:</strong> /api/users/{userId}</li>
    <li><strong>Consumes:</strong> JSON (application/json)</li>
    <li><strong>Produces:</strong> JSON (application/json)</li>
    <li><strong>Path Variable:</strong> userId - The ID of the user to get.</li>
    <li><strong>Response:</strong> Returns a UserResponse representing the user.</li>
    <li><strong>HTTP Status:</strong> 200 OK</li>
</ul>

<h4>Update Some User Fields</h4>
<ul>
    <li><strong>HTTP Method:</strong> PATCH</li>
    <li><strong>Endpoint:</strong> /api/users/{userId}</li>
    <li><strong>Consumes:</strong> JSON (application/json)</li>
    <li><strong>Produces:</strong> JSON (application/json)</li>
    <li><strong>Path Variable:</strong> userId - The ID of the user to update.</li>
    <li><strong>Request Body:</strong> A JSON object representing the fields to update (UserUpdateSomeFieldsRequest).</li>
    <li><strong>Validation:</strong> Validates the request body against the UserUpdateSomeFieldsRequest class using @Valid.</li>
    <li><strong>Response:</strong> Returns a UserResponse representing the updated user.</li>
    <li><strong>HTTP Status:</strong> 200 OK</li>
</ul>

<h4>Update All User Fields</h4>
<ul>
    <li><strong>HTTP Method:</strong> PUT</li>
    <li><strong>Endpoint:</strong> /api/users/{userId}</li>
    <li><strong>Consumes:</strong> JSON (application/json)</li>
    <li><strong>Produces:</strong> JSON (application/json)</li>
    <li><strong>Path Variable:</strong> userId - The ID of the user to update.</li>
    <li><strong>Request Body:</strong> A JSON object representing all user fields to update (UserUpdateAllFieldsRequest).</li>
    <li><strong>Validation:</strong> Validates the request body against the UserUpdateAllFieldsRequest class using @Valid.</li>
    <li><strong>Response:</strong> Returns a UserResponse representing the updated user.</li>
    <li><strong>HTTP Status:</strong> 200 OK</li>
</ul>

<h4>Delete User</h4>
<ul>
    <li><strong>HTTP Method:</strong> DELETE</li>
    <li><strong>Endpoint:</strong> /api/users/{userId}</li>
    <li><strong>Path Variable:</strong> userId - The ID of the user to delete.</li>
    <li><strong>Response:</strong> Deletes the user with the specified ID.</li>
    <li><strong>HTTP Status:</strong> 204 No Content</li>
</ul>

<h4>Search for Users by Birth Date Range</h4>
<ul>
    <li><strong>HTTP Method:</strong> GET</li>
    <li><strong>Endpoint:</strong> /api/users</li>
    <li><strong>Request Parameters:</strong>
        <ul>
            <li>from (Required): The start date of the birth date range in ISO date format (yyyy-MM-dd).</li>
            <li>to (Required): The end date of the birth date range in ISO date format (yyyy-MM-dd).</li>
        </ul>
    </li>
    <li><strong>Response:</strong> Returns a List of UserResponses within the specified birth date range.</li>
    <li><strong>HTTP Status:</strong> 200 OK</li>
</ul>

<h3 id="technologies-used">Technologies Used</h3>

  <ul>
        <li>Java 17</li>
        <li>Spring Boot 3.2.5</li>
        <li>Gradle</li>
        <li>Spring Data JPA</li>
        <li>Spring Web</li>
        <li>Spring Validation</li>
        <li>Spring Boot Hateoas</li>
        <li>Mapstruct</li>
        <li>Lombok</li>
        <li>PostgreSQL</li>
        <li>Docker</li>
        <li>Testcontainers</li>
        <li>JUnit</li>
        <li>Mockito</li>
  </ul>
