package services
 
import javax.inject._
import models._
 
import scala.concurrent.Future
 
class MovieService @Inject() (movieTable: MovieTable) {
 
  def addMovieService(movie: Movie): Future[String] = {
    movieTable.addMovie(movie)
  }
 
  def getMovieService(title: String): Future[Seq[Movie]] = {
    movieTable.getMovie(title)
  }
 
  def listAllService: Future[Seq[Movie]] = {
    movieTable.listAll
  }

  def listAllSortedWithGenreService(genre: String): Future[Seq[Movie]] = {
    movieTable.listAllSortedWithGenre(genre)
  }

  def listAllSortedService(): Future[Seq[Movie]] = {
    movieTable.listAllSorted()
  }

  def getMoviesCountPerYearService(): Future[Seq[(Int, Int)]] = {
    movieTable.getMoviesCountPerYear()
  }
}