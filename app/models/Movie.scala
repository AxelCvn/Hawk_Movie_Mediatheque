package models

import java.sql.Date

import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import play.api.data.Forms._
import play.api.data.Form

import javax.inject.Inject

import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import scala.concurrent.{ExecutionContext, Future}

/** Movie class */
case class Movie(
    id: Long = 0L,
    title: String, 
    country: String, 
    year: Int, 
    original_title: Option[String], 
    french_release: Option[Date], 
    synopsis: Option[String], 
    genre: String, 
    ranking: Int
)

/** The form used in API to create movie  
 *
 *  This allows to create a form.
 *  This allows to get Movie data from a form
 * and vice-versa
 */
object MovieForm {
    import play.api.data.Forms._
    import play.api.data.Form



    case class MovieData(
        title: String, 
        country: String, 
        year: Int, 
        original_title: Option[String], 
        french_release: Option[Date], 
        synopsis: Option[String], 
        genre: String, 
        ranking: Int
        )

    val movieForm = Form(
        mapping(
            "title" -> nonEmptyText(maxLength=250),
            "country" -> nonEmptyText(maxLength=3), //TODO use ISO
            "year" -> number,
            "original_title" -> optional(nonEmptyText(maxLength=250)),
            "french_release" -> optional(sqlDate("yyyy-MM-dd")),
            "synopsis" -> optional(text),
            "genre" -> nonEmptyText(maxLength=250),  //TODO use list(nonEmptyText(maxLength(50)))
            "ranking" -> number(min = 0, max = 10)
            )(MovieData.apply)(MovieData.unapply) 
    )
}

/** Movie class as Database Table
 *
 *  This allows to create a Movie instance from
 *  a row of the database table.
 *  An create a table row from a Movie instance
 */
class MovieTableDef(tag: Tag) extends Table[Movie](tag, "movie") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def country = column[String]("country")
  def year = column[Int]("year")
  def original_title = column[Option[String]]("original_title")
  def french_release = column[Option[Date]]("french_release")
  def synopsis = column[Option[String]]("synopsis")
  def genre = column[String]("genre")
  def ranking = column[Int]("ranking")
  def * = (id, title, country, year, original_title, french_release, synopsis, genre, ranking) <> (Movie.tupled, Movie.unapply(_))
}

/** Movie class used to interact with database */
class MovieTable @Inject()(protected val dbConfigProvider: DatabaseConfigProvider
)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {
        var movieTable = TableQuery[MovieTableDef]

        val schema = movieTable.schema
        val setup = schema.create
        val setupFuture = db.run(setup)

        /** Returns all movies */
        def listAll: Future[Seq[Movie]] = {
            dbConfig.db.run(movieTable.result)
        }
        
        /** Returns all movies of a given genre sorted by years and title desc */
        def listAllSortedWithGenre(genre: String): Future[Seq[Movie]] = {
            dbConfig.db.run(movieTable.filter(_.genre like s"%${genre}%").sortBy(m => (m.year, m.title)).result)
        }

        /** Returns all movies sorted by years and title desc */
        def listAllSorted(): Future[Seq[Movie]] = {
            dbConfig.db.run(movieTable.sortBy(m => (m.year, m.title)).result)
        }

        /** Returns all movies with given title */
        def getMovie(title: String): Future[Seq[Movie]] = {
            printf(title.toString())
            dbConfig.db
                .run(movieTable
                .filter(_.title === title)
                .result)
        }

        /** Add a movie to the mediathequen return the list of all movies */
        def addMovie(movie: Movie): Future[String] = {
            dbConfig.db
                .run(movieTable += movie)
                .map(res => "Movie successfully added")
                .recover{
                    case ex: Exception => {
                        printf(ex.getMessage())
                        ex.getMessage()
                    }
                }
        }

        /** Returns the count of movies for each year */
        def getMoviesCountPerYear(): Future[Seq[(Int, Int)]] = {
            dbConfig.db
                .run(movieTable
                    .map(m => (m.year, m.title))
                    .groupBy(_._1)
                    .map { case (year, titleList) =>
                        (year, titleList.length)}.result)
        }
    }