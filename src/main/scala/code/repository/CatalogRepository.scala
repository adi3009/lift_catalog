package code.repository

// Use H2Driver to connect to an H2 database
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import code.model._
import scala.concurrent.Future

object CatalogRepository {

  case class Testing(id: Option[Long], params: Seq[Long])

  implicit val seqToStringColumn = MappedColumnType.base[Seq[Long], String](
    { s => s.mkString(",") },
    { str => str.split(",").map(_.trim.toLong) })

  class TestingTable(tag: Tag) extends Table[Testing](tag, "testing") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def params = column[Seq[Long]]("params")
    def * = (id, params) <> (Testing.tupled, Testing.unapply)
  }

  private class CategoryTable(tag: Tag) extends Table[Category](tag, "categories") {

    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def urlKey = column[String]("url_key")

    def description = column[String]("description")

    def * = (id, name, urlKey, description) <> (Category.tupled, Category.unapply)
  }

  private val categories = TableQuery[CategoryTable]

  private class ProductTable(tag: Tag) extends Table[Product](tag, "products") {

    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def description = column[String]("description")

    def categoryId = column[Option[Long]]("category_id")

    def inCategories = foreignKey("category_fk", categoryId, categories)(_.id, ForeignKeyAction.NoAction, ForeignKeyAction.SetNull)

    def * = (id, name, description, categoryId) <> (Product.tupled, Product.unapply)
  }

  private val products = TableQuery[ProductTable]

  def setup = {
    val actions = DBIO.seq(
      (categories.schema ++ products.schema).create,
      categories ++= sampleCategories,
      products ++= sampleProducts)

    Await.result(db.run(actions), Duration.Inf)
  }

  private val sampleCategories = Seq(
    Category(Some(1), "Women", "womens", "Womens category"),
    Category(Some(2), "Men", "mens", "Mens category"),
    Category(Some(3), "Fashion", "fashion", "Fashion category"))

  private val sampleProducts = for {
    c <- sampleCategories
    p <- 1 to 33
  } yield Product(None, s"Product ${p} ${c.name}", s"Product description for product in ${c.name} category", c.id)

  //val db = Database.forURL("jdbc:h2:mem:catalog;MODE=PostgreSQL;DB_CLOSE_DELAY=-1", Map("driver" -> "org.h2.Driver", "connectionPool" -> "disabled", "keepAliveConnection" -> "true"))

  val db = Database.forConfig("in_mem_catalog")

  def allCategories = {
    val cs = for {
      c <- categories
    } yield (c)

    Await.result(db.run(cs.result), Duration.Inf)
  }

  def categoryByUrlKey(urlKey: String) = {
    val cat = for {
      c <- categories if c.urlKey === urlKey
    } yield (c)

    Await.result(db.run(cat.result), Duration.Inf).headOption
  }

  def productById(id: Long): Future[Option[Product]] = {
    val product = for {
      p <- products if p.id === id
    } yield p

    db.run(product.result.headOption)
  }

  def categoryProducts(categoryId: Long) = {
    val pdts = for {
      p <- products if p.categoryId === categoryId
    } yield p

    Await.result(db.run(pdts.result), Duration.Inf)
  }

}
