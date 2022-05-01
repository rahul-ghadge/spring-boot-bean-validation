# spring-boot-request-validation

This project explains request validation for CRUD (**C**reate, **R**ead, **U**pdate, **D**elete) operations using spring boot and H2 in-memory database.
In this app we are using Spring Data JPA for built-in methods to do CRUD operations.     
`@EnableJpaRepositories` annotation is optional here to which enables H2 DB related configuration, which will read properties from `application.yml` file.


## Prerequisites 
- Java
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/guides/index.html)
- [H2 Database](https://www.h2database.com/html/main.html)
- [Lombok](https://objectcomputing.com/resources/publications/sett/january-2010-reducing-boilerplate-code-with-project-lombok)


## Tools
- Eclipse or IntelliJ IDEA (or any preferred IDE) with embedded Maven
- Maven (version >= 3.6.0)
- Postman (or any RESTful API testing tool)


<br/>


###  Build and Run application
_GOTO >_ **~/absolute-path-to-directory/spring-boot-request-validation**  
and try below command in terminal
> **```mvn spring-boot:run```** it will run application as spring boot application

or
> **```mvn clean install```** it will build application and create **jar** file under target directory 

Run jar file from below path with given command
> **```java -jar ~/path-to-spring-boot-request-validation/target/spring-boot-request-validation-0.0.1-SNAPSHOT.jar```**

Or
> run main method from `SpringBootValidationApplication.java` as spring boot application.  



### Code Snippets
1. #### Maven Dependencies
    Need to add below dependencies to enable H2 DB related config in **pom.xml**. Lombok's dependency is to get rid of boiler-plate code.   
    ```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
   
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
   
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
   
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    ```
   
  
   
2. #### Properties file
    Reading H2 DB related properties from **application.properties** file and configuring JPA connection factory for H2 database.  

    **src/main/resources/application.yml**
     ```
     server:
       port: 8090
     
     spring:
       h2:
         console:
           enabled: true
       datasource:
         url: jdbc:h2:mem:sampledb 
     ```
   
   
3. #### Model class
    Below are the model classes which we will store in H2 DB and perform CRUD operations.  
    **SuperHero.java**  
    ```
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Entity
    @Table
    public class SuperHero implements Serializable {
    
        @Id
        @GeneratedValue
        private int id;
    
        private String name;
        private String superName;
        private String profession;
        private int age;
        private boolean canFly;
    
    }
    ```
  
   **SuperHeroDto.java**
   ```
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class SuperHeroDto implements Serializable {
   
       private int id;
       @NotBlank(message = "Name shouldn't be empty or null")
       private String name;
       private String superName;
       @NotNull(message = "Profession shouldn't be null")
       private String profession;
       @Min(18)
       @Max(60)
       private int age;
       private boolean canFly;
   
   //    @Email - for email validation
   //    @NotNull - for not null validation
   //    @Pattern(regexp = "^\\d{10}$", message = "Invalid mobile number") - for mobile no validation
   
   }
   ```
   
4. #### CRUD operation for Super Heroes

    In **SuperHeroController.java** class, 
    we have exposed 5 endpoints for basic CRUD operations
    - GET All Super Heroes
    - GET by ID
    - POST to store Super Hero in DB
    - PUT to update Super Hero
    - DELETE by ID
    
    ```
    @RestController
    @RequestMapping(SuperHeroController.SUPERHERO_URL)
    @RequiredArgsConstructor
    public class SuperHeroController {
        
        public static final String SUPERHERO_URL = "/superhero";
        @GetMapping
        public ResponseEntity<List<?>> findAll();
    
        @GetMapping("/{id}")
        public ResponseEntity<?> findById(@PathVariable String id);
    
        @PostMapping
        public ResponseEntity<?> save(@RequestBody @Valid SuperHero superHero);
    
        @PutMapping("/{id}")
        public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid SuperHero superHero);
    
        @DeleteMapping("/{id}")
        public ResponseEntity<?> delete(@PathVariable String id);
    }
    ```
   
   In **SuperHeroServiceImpl.java**, we are autowiring above interface using `@RequiredArgsContructor` annotation and doing CRUD operation.
   <br/>
   `@Valid` annotation from `javax.validation.Valid` package will validate input request object `SuperHeroDto` and return response with binding result errors 
   thrown by spring if request doesn't full fill the criteria(annotations provided on each property from SuperHeroDto class). 
   <br/> 
   
   
5. #### Handling MethodArgumentNotValidException
    When request will not be valid as per `SuperHeroDto` criteria, spring validation will throw `MethodArgumentNotValidException`.
    We are capturing this exception under **SuperHeroExceptionHandler.java** class with the help of `@RestControllerAdvice` and `@ExceptionHandler` annotations.
    ```
    @RestControllerAdvice
    public class SuperHeroExceptionHandler {
    
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleInvalidArgument(MethodArgumentNotValidException ex) {
    //        Map<String, String> errors = new HashMap<>();
    //        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    //        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    
            Map<String, Object> errors = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            errors.put("status", HttpStatus.BAD_REQUEST.value());
    
            return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
        }
    
    
        @ResponseStatus(HttpStatus.NOT_FOUND)
        @ExceptionHandler(SuperHeroNotFound.class)
        public ResponseEntity<?> handleSuperHeroNotFound(SuperHeroNotFound ex) {
            Map<String, Object> errors = new HashMap<>();
            errors.put("status", HttpStatus.NOT_FOUND.value());
            errors.put("message", ex.getLocalizedMessage());
            return new ResponseEntity(errors, HttpStatus.NOT_FOUND);
        }
    }
    ```
   
   <br/>
  
   In **SuperHeroRepository.java**, we are extending `JpaRepository<Class, ID>` interface which enables CRUD related methods.
   <br/>
   ```
   public interface SuperHeroRepository extends JpaRepository<SuperHero, String> {
   }
   ```
  
   In **SuperHeroServiceImpl.java**, we are autowiring above interface using `@RequiredArgsContructor` annotation and doing CRUD operation.


    
### API Endpoints

- #### Super Hero CRUD Operations
    > **GET Mapping** http://localhost:8088/superhero  - Get all Super Heroes
    
    > **GET Mapping** http://localhost:8088/superhero/1  - Get Super Hero by ID
       
    > **POST Mapping** http://localhost:8088/superhero  - Add new Super Hero in DB  
    
     Request Body  
     ```
        {
            "name": "Tony",
            "superName": "Iron Man",
            "profession": "Business",
            "age": 50,
            "canFly": true
        }
     ```
    
    > **PUT Mapping** http://localhost:8088/superhero/3  - Update existing Super Hero for given ID 
                                                       
     Request Body  
     ```
        {
            "id": "3"
            "name": "Tony",
            "superName": "Iron Man",
            "profession": "Business",
            "age": 50,
            "canFly": true
        }
     ```
    
    > **DELETE Mapping** http://localhost:8088/superhero/4  - Delete Super Hero by ID

    > **POST Mapping** http://localhost:8088/superhero  - Invalid request
        
    Request Body  
    ```
    {
        "name": "",
        "superName": "Captain America",
        "profession": null,
        "age": 90,
        "canFly": true
    }
    ```
    Response Body - Status: 400-Bad Request
    ```
    {
      "profession": "Profession shouldn't be null",
      "name": "Name shouldn't be empty or null",
      "age": "must be less than or equal to 60",
      "status": 400
    }
    ```
