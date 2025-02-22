package web.tools

import web.domain.Person
import web.domain.Photo
import web.domain.Production

case class SiteBuilderOptions(
  xmlDir: String = "/Users/marc/wrk/projects/pinokkelijn/src/main/resources/xml",
  sourceDir: String = "/Users/marc/wrk/projects/pinokkelijn/src/main/resources",
  stagingDir: String = "/Users/marc/wrk/staging/pinokkelijn",
  target: String = "web",
  images: Boolean = true,
  root: Boolean = true,
  persons: Boolean = true,
  productions: Boolean = true,
  internet: Boolean = false,
  proxyHost: Option[String] = None,
  proxyPort: Int = 80,
  validate: Boolean = false
) {

  def imageSourceDir = "/Users/marc/wrk/projects/web/wrk/images"

  def rootDir: String = stagingDir + "/"

  def personsDir: String = rootDir + "personen" + "/"

  def personPhotosSmallDir: String = personsDir + "fotos-klein/"

  def personPhotosLargeDir: String = personsDir + "fotos-groot/"

  def productionsDir: String = rootDir + "producties" + "/"

  def dir(productionId: String): String = productionsDir + productionId + "/"

  def smallPhotosDir(productionId: String): String = dir(productionId) + "fotos-klein/"

  def largePhotosDir(productionId: String): String = dir(productionId) + "fotos-groot/"

  def smallPhotoKey(photo: Photo): String = s"producties/${photo.productionId}/fotos-klein/${photo.id}.jpg"

  def largePhotoKey(photo: Photo): String = s"producties/${photo.productionId}/fotos-groot/${photo.id}.jpg"

  def smallPosterKey(production: Production): String = s"producties/${production.id}/fotos-klein/${production.id}-poster.jpg"

  def largePosterKey(production: Production): String = s"producties/${production.id}/fotos-groot/${production.id}-poster.jpg"


  def isWeb: Boolean = "web".equals(target)

  def photoFile(photo: Photo): String = s"$imageSourceDir/productions/${photo.productionId}/gallery/${photo.id}.jpg"

  def smallPhotoFile(photo: Photo): String = s"${smallPhotosDir(photo.productionId)}${photo.id}.jpg"

  def largePhotoFile(photo: Photo): String = s"${largePhotosDir(photo.productionId)}${photo.id}.jpg"

  def productionSourceDir(production: Production): String = s"$imageSourceDir/productions/${production.id}/"

  def posterSourceFile(production: Production): String = s"${productionSourceDir(production)}${production.id}-poster.jpg"

  def smallPosterFile(production: Production): String = s"${smallPhotosDir(production.id)}${production.id}-poster.jpg"

  def largePosterFile(production: Production): String = s"${largePhotosDir(production.id)}${production.id}-poster.jpg"

  def posterFile(production: Production): String = s"${dir(production.id)}${production.id}-poster.jpg"

  def personSourcePhoto(person: Person): String = s"$imageSourceDir/persons/${person.key}.jpg"

  def smallPersonPhotoFile(person: Person): String = s"${personsDir}fotos-klein/${person.key}.jpg"

  def largePersonPhotoFile(person: Person): String = s"${personsDir}fotos-groot/${person.key}.jpg"

  def smallPersonPhotoName(person: Person): String = s"personen/fotos-klein/${person.key}.jpg"

  def largePersonPhotoName(person: Person): String = s"personen/fotos-groot/${person.key}.jpg"

}
