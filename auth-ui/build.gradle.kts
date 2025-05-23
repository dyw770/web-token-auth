
tasks.register<Exec>("build") {
  group = BasePlugin.BUILD_GROUP
  description = "删除项目构建文件"
  outputs.upToDateWhen { false }
  environment = System.getenv() as Map<String, Any>
  workingDir = project.projectDir

  val npm = findNpm()
  if (npm.isNullOrBlank()) {
    throw GradleException("未查找到系统中的 npm, 请先安装Nodejs或则设置环境变量")
  }
  commandLine(npm, "run", "build")
}

fun findNpm(): String? {
  // 获取系统的 PATH 环境变量
  val pathEnv = System.getenv("PATH") ?: return null

  // 根据操作系统拆分路径分隔符（Windows 是 ";", Unix 是 ":"）
  val pathSeparator = if (System.getProperty("os.name").contains("Windows", ignoreCase = true)) ";" else ":"
  val paths = pathEnv.split(pathSeparator)

  // 可能的 npm 可执行文件名
  val executableNames = if (System.getProperty("os.name").contains("Windows", ignoreCase = true)) listOf("npm.cmd", "npm") else listOf("npm")

  // 遍历所有路径，寻找 npm
  for (dir in paths) {
    for (exe in executableNames) {
      val fullPath = File(dir, exe).absolutePath
      if (File(fullPath).isFile && File(fullPath).canExecute()) {
        return fullPath
      }
    }
  }
  return null
}
