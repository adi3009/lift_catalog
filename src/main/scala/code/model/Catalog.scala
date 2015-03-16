package code.model

import net.liftweb.common._
import net.liftweb.record._
import net.liftweb.record.field._
import net.liftweb.squerylrecord.KeyedRecord
import org.squeryl.Schema
import org.squeryl.annotations.Column

class Category private () extends Record[Category] with KeyedRecord[Long] {

  def meta = Category

  @Column(name = "id")
  val idField = new LongField(this)

  val parentId = new OptionalLongField(this)

  val name = new StringField(this, "")

  val urlKey = new StringField(this, "")

  val description = new StringField(this, "")

}

object Category extends Category with MetaRecord[Category]

class Product private () extends Record[Product] with KeyedRecord[Long] {

  def meta = Product

  @Column(name = "id")
  val idField = new LongField(this)

  val name = new StringField(this, "")

  val categoryId = new OptionalLongField(this)
}

object Product extends Product with MetaRecord[Product]

import net.liftweb.squerylrecord.RecordTypeMode._

object CatalogSchema extends Schema {

  val categories = table[Category]("categories")

  val products = table[Product]("products")

  lazy val setup = inTransaction {
    this.printDdl
    this.create
  }

  lazy val populate = {
    inTransaction {
      categories.insert(List(
        Category.createRecord.name("Women").urlKey("women").description("Women category"),
        Category.createRecord.name("Men").urlKey("men").description("Fashion category"),
        Category.createRecord.name("Fashion").urlKey("fashion").description("Fashion category")))
    }

    inTransaction {
      val womenCatId = Catalog.categoryByUrlKey("women").openOrThrowException("its just demo").idField.get
      val menCatId = Catalog.categoryByUrlKey("men").openOrThrowException("its just demo").idField.get
      val fashionCatId = Catalog.categoryByUrlKey("fashion").openOrThrowException("its just demo").idField.get

      categories.insert(List(
        Category.createRecord.name("Bags").urlKey("bags").description("Womens Bags").parentId(Full(womenCatId)),
        Category.createRecord.name("Boots").urlKey("boots").description("Womens Boots").parentId(Full(womenCatId)),
        Category.createRecord.name("Shoe").urlKey("shoe").description("Mens Shoe").parentId(Full(menCatId))))
    }
    
    // not the best, but fits the purpose here
    val randomGen = new scala.util.Random(5)

    def randomCategoryId = {
      val n = randomGen.nextInt(6)
      if (n <= 0 || n > 5) 1L else n.toLong
    }

    lazy val randomProducts: Seq[Product] = for {
      i <- 1 to 99
    } yield {
      val id = randomCategoryId
      val category = categories.find(_.id == id)
      category match {
        case Some(c) => Product.createRecord.name(s"Product ${i} ${c.name}").categoryId(c.id)
        case None    => Product.createRecord.name(s"Product ${i}").categoryId(id)
      }
    }

    inTransaction {
      products.insert(randomProducts)
    }
  }
}

object Catalog {

  def categoryByUrlKey(urlKey: String): Box[Category] = inTransaction {
    //val query = from(categories)(c => where(c.urlKey === urlKey) select (c))
    //query.headOption
    CatalogSchema.categories.find(_.urlKey.toString() == urlKey)
  }

  def productById(id: String): Box[Product] = inTransaction {
    CatalogSchema.products.find(_.idField.toString() == id)
  }

  def categoryProducts(categoryId: Long) = inTransaction {
    CatalogSchema.products.filter { _.categoryId.toString() == categoryId }
  }

  lazy val categoryHierarchy = inTransaction {
    val allCategories = CatalogSchema.categories.toList
    for {
      p <- allCategories if p.parentId.toString() == ""
    } yield (p, allCategories.filter { _.parentId == p.idField })
  }

  /*lazy val womens = Category(1, None, "Womens", "womens", "Womens category")

  lazy val mens = Category(2, None, "Mens", "mens", "Mens category")

  lazy val fashion = Category(3, None, "Fashion", "fashion", "Fashion category")

  lazy val bags = Category(4, Some(1), "Bags", "womens-bags", "Womens bags category")

  lazy val boots = Category(5, Some(1), "Boots", "womens-boots", "Womens boots category")

  lazy val shoes = Category(5, Some(2), "Shoes", "mens-shoes", "Mens shoes category")

  lazy val categories = List(womens, mens, fashion, bags, boots, shoes)

  lazy val categoryHierarchy = List(womens -> List(bags, boots), mens -> List(shoes), fashion -> Nil)

  def categoryByUrlKey(urlKey: String): Box[Category] = categories.find(urlKey == _.urlKey)

  def productById(id: String): Box[Product] = products.find(id == _.id.toString)

  lazy val products: Seq[Product] = for {
    i <- 1 to 99
  } yield {
    val id = randomCategoryId
    val category = categories.find(_.id == id)
    category match {
      case Some(c) => Product(i, s"Product ${i} ${c.name}", c.id)
      case None    => Product(i, s"Product ${i}", id)
    }
  }

  // not the best, but fits the purpose here
  val randomGen = new scala.util.Random(5)

  def randomCategoryId = {
    val n = randomGen.nextInt(6)
    if (n <= 0 || n > 5) 1L else n.toLong
  }*/
}