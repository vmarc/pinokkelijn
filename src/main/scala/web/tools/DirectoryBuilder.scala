package web.tools

import web.common.Util.mkdir
import web.domain.Site

class DirectoryBuilder(site: Site, config: SiteBuilderOptions) {

  def build(): Unit = {

    mkdir(config.rootDir)
    mkdir(config.personsDir)
    mkdir(config.productionsDir)

    mkdir(config.personPhotosSmallDir)
    mkdir(config.personPhotosLargeDir)

    site.productions foreach { production =>
      mkdir(config.dir(production.id))
      mkdir(config.smallPhotosDir(production.id))
      mkdir(config.largePhotosDir(production.id))
    }

    mkdir(config.rootDir + "images")
  }
}
