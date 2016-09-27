# README #

Android Library to build an Android style Form from a JSON object.
The individual form elements can be used independently of the form as well.

### How do I get set up? ###

###  Gradle Configuration### 
* Add the following to your app .gradle 

1. Add Dependency 
 
```
dependencies {
compile 'com.giift:formr:2016.09.+'
}
```

2.  Configure Repository
```
repositories {
    maven {
        url 'https://api.bitbucket.org/1.0/repositories/giiftalldev/giiftsdk-maven/raw/master'
        credentials {
            username getRepositoryUsername()
            password getRepositoryPassword()
        }
    }
    flatDir {
        dirs 'libs'
    }
}

```






* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact