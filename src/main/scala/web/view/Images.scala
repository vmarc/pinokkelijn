package web.view

import web.Config
import web.domain.ImageDimension
import web.domain.Person
import web.domain.Photo
import web.domain.Production

class Images(config: Config, imageDimensions: Map[String, ImageDimension]) {

  def apply(photo: Photo): String = {
    val src = config.photoFile(photo)
    val dimension = imageDimensions(src)
    val alt = photo.productionTitleText
    image(src, dimension, alt)
  }

  def apply(person: Person): String = {
    val src = config.largePersonPhoto(person)
    val dimension = imageDimensions(src)
    val alt = person.name
    image(src, dimension, alt)
  }

  def small(person: Person): String = {
    val src = config.smallPersonPhoto(person)
    val dimension = imageDimensions(src)
    val alt = person.name
    image(src, dimension, alt)
  }

  def exists(person: Person): Boolean = {
    val src = config.largePersonPhoto(person)
    imageDimensions.contains(src)
  }

  def smallImageDimension(person: Person): ImageDimension = {
    val src = config.smallPersonPhoto(person)
    imageDimensions(src)
  }

  def apply(src: String, alt: String): String = {
    val dimension = imageDimensions(src)
    image(src, dimension, alt)
  }

  def apply(root: String, src: String, alt: String): String = {
    val dimension = imageDimensions(src)
    image(root + "/" + src, dimension, alt)
  }

  def hasPoster(production: Production): Boolean = imageDimensions.contains(config.smallPosterKey(production))

  def smallPoster(production: Production): String = {
    val src = "fotos-klein/%s-poster.jpg".format(production.id)
    val dimension = imageDimensions(config.smallPosterKey(production))
    val alt = "Affiche: " + production.longTitle
    image(src, dimension, alt)
  }

  def largePoster(production: Production): String = {
    val src = "fotos-groot/%s-poster.jpg".format(production.id)
    val dimension = imageDimensions(config.largePosterKey(production))
    val alt = "Affiche: " + production.longTitle
    image(src, dimension, alt)
  }

  def smallPosterFile(production: Production): String = {
    val src = config.smallPosterKey(production)
    val dimension = imageDimensions(config.smallPosterKey(production))
    val alt = "Affiche: " + production.longTitle
    image(src, dimension, alt)
  }

  def smallProductionPhoto(photo: Photo): String = {
    val imageKey = config.smallPhotoKey(photo)
    val dimension = imageDimensions(imageKey)
    val alt = photo.id
    val src = "fotos-klein/%s.jpg".format(photo.id)
    image(src, dimension, alt)
  }

  def smallPersonPhoto(photo: Photo): String = {
    val imageKey = config.smallPhotoKey(photo)
    val dimension = imageDimensions(imageKey)
    val alt = photo.id
    val src = "../producties/%s/fotos-klein/%s.jpg".format(photo.productionId, photo.id)
    image(src, dimension, alt)
  }

  def largePersonPhoto(photo: Photo): String = {
    val imageKey = config.largePhotoKey(photo)
    val dimension = imageDimensions(imageKey)
    val alt = photo.id
    val src = "../producties/%s/fotos-groot/%s.jpg".format(photo.productionId, photo.id)
    image(src, dimension, alt)
  }

  def hasPerson(person: Person): Boolean = imageDimensions.contains(config.largePersonPhoto(person))

  def person(person: Person): String = {
    val dimension = imageDimensions(config.largePersonPhoto(person))
    val alt = person.name
    val src = "fotos-groot/%s.jpg".format(person.key)
    image(src, dimension, alt)
  }

  def image(src: String, dimension: ImageDimension, alt: String): String = {
    s"""<img src="$src" width="${dimension.width}" height="${dimension.height}" title="$alt" alt="$alt"/>"""
  }
}
