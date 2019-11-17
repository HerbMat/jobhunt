[![Codacy Badge](https://api.codacy.com/project/badge/Grade/064e0c246df0427f8767688014de0a26)](https://www.codacy.com/manual/matikz1110/jobhunt?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=HerbMat/jobhunt&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.com/HerbMat/jobhunt.svg?branch=master)](https://travis-ci.com/HerbMat/jobhunt.svg?branch=master)


## Simple Job hunter application for DNA Technology

Main goal of app is to expose RESTful API
(together with business logic and data layer) which manages two entities: a user and
job offers created by this user.

### Most important missing steps

 1. I think I should add authentication via JWT.
 
 2. Password should be hidden(not shown in logs etc.) and stored in char array.
 
 3. It should have improved exception handling for database errors.
 
 4. It should have improved validation. For example checking if enum has proper format.
 
 5. It should have improved swagger configuration and added option to turn it off on production environment via properties.
 
 6. Integration tests should be added.
 
 7. Better class hierarchy. I think there should be additional level because dna.jobhunt.service.impl.DefaultUserService.getUserByUsername 
 is not matching the rest of method return types. And dependency on the same level abstraction can backfire if the system would grow.

### Run tests

```console
gradle test
```

### Run application

```console
gradle bootRun
```