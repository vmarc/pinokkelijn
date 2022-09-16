package web.server.sitemap

case class SiteMapUrl(
  location: String,
  lastModified: String,
  changeFrequency: String,
  priority: String
)
