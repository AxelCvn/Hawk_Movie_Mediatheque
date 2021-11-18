package controllers

import javax.inject._

import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.data._
import play.api.mvc._
import play.api.i18n._
import play.api.libs.json._

import models.{Movie, MovieForm}
import services.MovieService


class MovieController @Inject()(cc: ControllerComponents, movieService: MovieService) extends AbstractController(cc) with I18nSupport {

    implicit val movieFormat = Json.format[Movie]

    def index = Action {
      Ok(views.html.index())
    }

    def forms() = Action {  implicit request: Request[AnyContent] =>
      Ok(views.html.movie(MovieForm.movieForm))
    }

    def getAllMovies() = Action.async { implicit request: Request[AnyContent] =>
        movieService.listAllService map { items =>
          Ok(Json.toJson(items))
        }
      }
    
    /** Return the list of all movies of a specified genre, sorted by year desc */
    def getAllMoviesSortedWithGenre(genre: String) = Action.async { implicit request: Request[AnyContent] =>
      movieService.listAllSortedWithGenreService(genre) map { items =>
        Ok(Json.toJson(items))
      }
    }

    /** Return the list of all movies sorted by year desc */
    def getAllMoviesSorted() = Action.async { implicit request: Request[AnyContent] =>
      movieService.listAllSortedService() map { items =>
        Ok(Json.toJson(items))
      }
    }

    /** Return the movies given a title */
    def getMovie(title: String) = Action.async { implicit request: Request[AnyContent] =>
      movieService.getMovieService(title) map { item =>
        Ok(Json.toJson(item))
      }
    }

    /** Returns the count of movies for each year (if there is at least one movie in this year) */
    def getMoviesCountPerYearService() = Action.async { implicit request: Request[AnyContent] =>
      movieService.getMoviesCountPerYearService() map { items =>
        Ok(Json.toJson(items))
      }
    }

    def movieFormPost() = Action.async { implicit request: Request[AnyContent] =>
      MovieForm.movieForm.bindFromRequest().fold(
        // Return a message in case of error
        formWithErrors => {
          formWithErrors.errors.foreach(println)
          Future.successful(BadRequest("An error occured. Please verify that you respect form's contraints"))
        },
        // Otherwise insert the movie and return the list of all movies updated with the new movie
        formData => {
          val newMovieItem = Movie(0L, formData.title, formData.country, formData.year, formData.original_title, formData.french_release, formData.synopsis, formData.genre, formData.ranking)
          movieService.addMovieService(newMovieItem).map( _ => Redirect(routes.MovieController.getAllMovies()))
        })
    }
}
