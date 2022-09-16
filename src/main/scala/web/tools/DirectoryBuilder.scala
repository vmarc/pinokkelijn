package web.tools

import org.apache.commons.io.FileUtils
import web.domain.Site

import java.io.File

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

  private def mkdir(dirname: String): Unit = {
    FileUtils.forceMkdir(new File(dirname))
  }
}
