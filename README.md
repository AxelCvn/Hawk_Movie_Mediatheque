# Movie Mediatheque

## Requirements

- Mysql Server 
You can create a new user so you don't have to use your root password in this exercise. More information can be found on [Mysql Ubuntu Doc](https://doc.ubuntu-fr.org/mysql)

- Scala 2.13.7+ installed

## Setup

- Connect to mysql and create a mysql database named `scalatestdb`

You can change this conf in file `conf/application.conf`

```
create database scalatestdb;
```
- Then run :

```
sbt run
```


## Use The Movie Mediatheque Api

- Open a navigator and go to http://localhost:9000

- Enjoy the different possibilities

## Easy testing of US

### US 1-1 : En tant qu'utilisateur, je veux ajouter un film à la médiathèque pour le retrouver ultérieurement.

Here you can either click on the `Add a movie to the mediatheque through a form` from http://localhost:9000 , complete the form and click on `submit` button or you can send a POST request with curl :

```
curl -v -d '{"title": "Mon Premier Film", "country": "USA", "year": "2021", "original_title": "My First Movie", "french_release": "2021-01-01", "synopsis": "Un synopsis qui raconte une histoire", "genre": "Example", "ranking": "5"}' -H 'Content-Type: application/json' -X POST localhost:9000/api/movie
```

NB : 

- You can find examples to populate your mediatheque at the end of this file
- Using the form should return the list of all movies from the mediatheque (as JSON) if you correctly added your movie, otherwise an error message will be returned.
- All constraints on fields are specified on the form.
- I couldn't manage to use an ISO 3166-1 alpha-3 (3 caractères)
- The date parameter is correctly inserted as "yyyy-MM-dd" in database eventhough the input format is not clear with the constraint and unfortu
nately it's not correctly returned either in the JSON response
- I couldn't manage to get the `original_title` field required only if country!="FRA" so I put it as required

To find the movie you inserted later, you can navigate to : localhost:9000/api/movies/{your movie name}

### US 1-2 : En tant qu'utilisateur, je veux lister les films préalablement enregistées, en filtrant par genre.

Here you can either click on the `Get All Movies Sorted` from http://localhost:9000 if you don't want to specify any genre or you can navigate to : localhost:9000/api/moviesSorted/{a genre} (if you don't put the genre parameter all movies are returned)

### US 1-3 : En tant qu'utilisateur, je veux connaitre le nombre de films présents dans la médiathèque par année de production.

Here you can either click on the `Get the count of movies per year` from http://localhost:9000 or you can navigate directly to : http://localhost:9000/api/moviesPerYear or send the GET request as follow :

```
curl localhost:9000/api/moviesPerYear
```

## Improvements TODO

- Hande the issues I encountered to complete the 2 first US correctly
- Add comments and documentation, improve the code to follow the scala style guide (https://docs.scala-lang.org/style/)
- Add tests
- Handle security properly (authentication etc.)
- Improve the views with some CSS (and JS to go further)

## Some movies to populate your mediatheque and get more interesting results

```
curl -v -d '{"title": "RRRrrrr!!!", "country": "FRA", "year": "2004", "original_title": "RRRrrrr!!!", "french_release": "2021-01-28", "synopsis": "Des gens qui se nomment Pierre", "genre": "Comédie", "ranking": "9"}' -H 'Content-Type: application/json' -X POST localhost:9000/api/movie
```

```
curl -v -d '{"title": "Tuer Bill", "country": "USA", "year": "2003", "original_title": "Kill Bill", "french_release": "2003-11-28", "synopsis": "Des gens avec des sabres", "genre": "Action", "ranking": "10"}' -H 'Content-Type: application/json' -X POST localhost:9000/api/movie
```

```
curl -v -d '{"title": "Le Roi Lion", "country": "USA", "year": "1994", "original_title": "The Lion King", "french_release": "1994-11-09", "synopsis": "Des lions dans la savane", "genre": "Comédie Dessin Animé", "ranking": "10"}' -H 'Content-Type: application/json' -X POST localhost:9000/api/movie
```

This example should fail : 

```
curl -v -d '{"title": "Exemple qui rate", "country": "ENG", "year": "2021", "original_title": "Failed example", "french_release": "2021-01-02", "synopsis": "Un synopsis qui raconte une histoire", "genre": "Example", "ranking": "5.5"}' -H 'Content-Type: application/json' -X POST localhost:9000/api/movie
```

# Hawk_Movie_Mediatheque
