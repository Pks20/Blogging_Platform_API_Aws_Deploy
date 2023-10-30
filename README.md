* Frameworks and language used
  
    * Springboot and java
* Data flow
  1. Controller
  
     * UserController
  2. Services
 
     * EmailHandler
     * PasswordEncrypter
     * AuthenticationService
     * CommentService
     * FollowService
     * PostService
     * UserService
  3. Repository
     * IAuthenticationRepo
     * ICommentRepo
     * IFollowRepo
     * IPostRepo
     * IUserRepo
  4. DataBase Design
  5. Model
     * AuthenticationToken
     * Comment
     * Follow
     * Post
     * User
  6. DataBase used in our project
     * MYSQL DataBase 
 

* # Project Summary
   In this project we have created Blogging_Platform_API project and used mysql database to store the data. This application follows MVC-architecture. The Model-View-Controller (MVC) is a well-known design pattern in the web development field. It is way to organize our code. It specifies that a program or application shall consist of data model, presentation information and control information. I have created this project by using spring initilizer and
  add required dependencies like lombok, spring web, Mysql, JPA, Email, swagger, Validation etc. User can signUp  this Blog Application for creation of account. After signin in account user  can comment, and Follow other user.
  User can post their blog in this application. User can update the Post using userId. Users can also search for posts and follow other users.
