package web.tools

import org.xml.sax.SAXParseException
import web.domain.Actor
import web.domain.Performance
import web.domain.Person
import web.domain.PersonDetail
import web.domain.Photo
import web.domain.Production
import web.domain.Site
import web.domain.Task

import java.io.File
import scala.xml.Node
import scala.xml.NodeSeq
import scala.xml.XML

class SiteParser(xmldir: String) {

  def site(): Site = {

    val personsByName = loadPersonsByName()
    val productions = loadProductions(personsByName)
    val persons = enrichPersons(personsByName, productions)

    Site(
      productions,
      persons
    )
  }

  private def loadProductions(personsByName: Map[String, Person]): List[Production] = {
    productionFiles.map { file =>
      val node = loadFile(file)

      try {
        val productionId = (node \ "@id").text
        Production(
          productionId,
          year = (node \ "@jaar").text,
          title = (node \ "@titel").text,
          shortDescription = (node \ "korte-beschrijving").text,
          description = ((node \ "beschrijving") \ "_").mkString,
          location = (node \ "plaats").text,
          performances = parsePerformances(node \ "opvoeringen"),
          actors = parseActors(personsByName, node \ "acteurs"),
          tasks = parseTasks(personsByName, node \ "taken"),
          articles = (node \ "tekst").map(a => (a \ "_").mkString),
          photos = parsePhotos(personsByName, node \ "fotos", productionId)
        )
      }
      catch {
        case e: RuntimeException =>
          val message = "Error parsing file \"%s\": %s".format(file.getName, e.getMessage)
          throw new RuntimeException(message, e)
      }
    }
  }

  def loadFile(file: File): Node = {
    try {
      XML.loadFile(file)
    }
    catch {
      case e: SAXParseException =>
        val message = "Error parsing file \"%s\" at line %d, column %d: %s".format(file.getName, e.getLineNumber, e.getColumnNumber, e.getMessage)
        throw new RuntimeException(message, e)
    }
  }

  private def parsePerformances(node: NodeSeq): Seq[Performance] = {
    (node \ "opvoering").map { performance =>
      Performance(performance.text)
    }
  }

  private def parseActors(personsByName: Map[String, Person], node: NodeSeq): Seq[Actor] = {
    (node \ "acteur").map { actor =>
      val name = (actor \ "@naam").text
      val person = personsByName.getOrElse(name, {
        throw new RuntimeException("Onbekende acteur: <" + name + ">, kontroleer spelling of voeg toe in personen.xml")
      })
      val role = (actor \ "@rol").text
      val description = actor.text
      Actor(person, role, description)
    }
  }

  private def parseTasks(personsByName: Map[String, Person], node: NodeSeq): Seq[Task] = {
    (node \ "taak").map { task =>
      val description = (task \ "@beschrijving").text
      val persons = (task \ "naam").map(_.text).map { name =>
        if (name.startsWith("*")) {
          Person("", name.drop(1), "", Seq.empty)
        }
        else {
          personsByName.getOrElse(name, {
            throw new RuntimeException("Onbekende persoon: <" + name + "> in taak <" + description + ">, kontroleer spelling of voeg toe in personen.xml")
          })
        }
      }
      new Task(description, persons)
    }
  }

  private def parsePhotos(personsByName: Map[String, Person], node: NodeSeq, productionId: String): Seq[Photo] = {
    (node \ "foto").map { photo =>
      val id = (photo \ "@id").text
      Photo(
        id,
        photographer = (photo \ "@fotograaf").text,
        description = (photo \ "beschrijving").text,
        web = (photo \ "@web").text == "true",
        show = (photo \ "@show").text == "true",
        persons = ((photo \ "personen") \ "naam").map(_.text).map { name =>
          if (name.startsWith("*")) {
            Person("", name.drop(1), "", Seq.empty)
          }
          else {
            personsByName.getOrElse(name, {
              throw new RuntimeException("Onbekende persoon: <" + name + "> in foto <" + id + ">, kontroleer spelling of voeg toe in personen.xml")
            })
          }
        },
        productionId = productionId
      )
    }
  }

  private def productionFiles: List[File] = {
    val dir = new File(s"$xmldir/productions")
    dir.listFiles().filterNot(_.getName.endsWith(".svn")).toList.map(_.getAbsolutePath).sorted.map(new File(_))
  }

  private def loadPersonsByName(): Map[String, Person] = {
    parsePersons().map(person => (person.name, person)).toMap
  }

  private def parsePersons(): Seq[Person] = {
    val persons = XML.loadFile(s"$xmldir/personen.xml")
    (persons \ "persoon").map {
      person =>
        val lastName = (person \ "@naam").text
        val firstName = (person \ "@voornaam").text
        val key = personKey(lastName, firstName)
        Person(key, lastName, firstName, Seq.empty)
    }
  }

  private def enrichPersons(personsByName: Map[String, Person], productions: Seq[Production]): Seq[Person] = {
    val enriched = personsByName.map { case (key, person) =>
      val personDetails = productions.reverse.flatMap { production =>
        production.persons.flatMap { p =>
          if (p == person) {
            val photos = production.photos(person)
            val contributions = production.contributions(person)
            Some(PersonDetail(production, photos, contributions))
          }
          else {
            None
          }
        }
      }
      if (personDetails.nonEmpty) {
        key -> person.copy(details = personDetails)
      }
      else {
        key -> person
      }
    }
    enriched.values.toList.sortWith(alphabetical)
  }

  private def alphabetical = {
    (person1: Person, person2: Person) =>
      if (person1.lastName == person2.lastName) {
        person1.firstName < person2.firstName
      }
      else {
        person1.lastName < person2.lastName
      }
  }

  private def personKey(lastName: String, firstName: String): String = {
    (firstName + " " + lastName)
      .replaceAll("@eacute;", "e")
      .replaceAll("@iuml;", "i")
      .replaceAll("@ouml;", "o")
      .replaceAll(" van ", " Van ")
      .replaceAll(" den ", " Den ")
      .replaceAll(" de ", " De ")
      .replaceAll("-", "")
      .replaceAll(" ", "")
  }
}
