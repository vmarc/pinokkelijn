package web.tools

import web.common.Util.exists

class SiteBuilderOptionsParser {

  def options(args: Array[String]): SiteBuilderOptions = {

    val parser = new scopt.OptionParser[SiteBuilderOptions]("SiteBuilder") {
      opt[String]('s', "source") action { (v, o) => o.copy(sourceDir = v) } text "source root directory"
      opt[String]('d', "destination") action { (v, o) => o.copy(stagingDir = v) } text "staging root directory"
      opt[String]('t', "target") action { (v, o) => o.copy(target = v) } text "staging target: 'cd' or 'web'"
      opt[Boolean]('i', "images") action { (b, o) => o.copy(images = b) } text "generate images"
      opt[Boolean]('r', "root") action { (b, o) => o.copy(root = b) } text "generate root directory pages"
      opt[Boolean]('p', "persons") action { (b, o) => o.copy(persons = b) } text "generate person pages"
      opt[Boolean]('x', "productions") action { (b, o) => o.copy(productions = b) } text "generate production pages"
    }

    val commandLineOptions: Option[SiteBuilderOptions] = parser.parse(args, SiteBuilderOptions())

    commandLineOptions.foreach { options =>
      options.target match {
        case "cd" => // OK
        case "web" => // OK
        case _ =>
          println("Please specify 'cd' or 'web' for --target parameter (default is 'web')")
          println(parser.usage)
          sys.exit(-1)
      }

      if (!exists(options.sourceDir)) {
        println("File in --source parameter does not exist")
        println(parser.usage)
        sys.exit(-1)
      }

      if (!exists(options.stagingDir)) {
        println("File in --destination parameter does not exist")
        println(parser.usage)
        sys.exit(-1)
      }

      if (!options.images && !options.root && !options.persons && !options.productions) {
        println("Please set one of the following to true:")
        println("  --images=true")
        println("  --root=true")
        println("  --persons=true")
        println("  --productions=true")
        println(parser.usage)
        sys.exit(-1)
      }
    }
    commandLineOptions.getOrElse(sys.exit(-1))
  }
}
